package com.numtory.application.features.gold.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.features.gold.domain.usecase.FilterGoldMarketUseCase
import com.numtory.application.features.gold.domain.usecase.FilterGoldParams
import com.numtory.application.features.gold.domain.usecase.GetAppGoldExchangesUseCase
import com.numtory.application.features.gold.domain.usecase.GetDaricPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetDigikalaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldikaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetHamrahGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMelliGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMilliPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTalaseaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTechnoGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTlynPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetWallGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.SortGoldMarketUseCase
import com.numtory.application.features.gold.domain.usecase.SortGoldParams
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.ui.theme.REFRESH_TIMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class GoldMarketsViewModel
constructor(
    private val getDigikalaPriceUseCase: GetDigikalaPriceUseCase,
    private val getGoldikaPriceUseCase: GetGoldikaPriceUseCase,
    private val getTlynPriceUseCase: GetTlynPriceUseCase,
    private val getHamrahGoldPriceUseCase: GetHamrahGoldPriceUseCase,
    private val getMelliGoldPriceUseCase: GetMelliGoldPriceUseCase,
    private val getTalaseaPriceUseCase: GetTalaseaPriceUseCase,
    private val getWallGoldPriceUseCase: GetWallGoldPriceUseCase,
    private val getMilliPriceUseCase: GetMilliPriceUseCase,
    private val getTechnoGoldPriceUseCase: GetTechnoGoldPriceUseCase,
    private val getDaricPriceUseCase: GetDaricPriceUseCase,

    private val sortMarketUseCase: SortGoldMarketUseCase,
    private val filterMarketUseCase: FilterGoldMarketUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeGoldExchangeUseCase,
    private val getMarketAvgUseCase: GetGoldMarketAvgUseCase,
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppGoldExchangesUseCase,
    private val removeInvalidExchangeUseCase: RemoveInvalidGoldExchangeUseCase,
) : ViewModel() {

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    var allMarkets: MutableList<GoldMarketPrice> = mutableListOf()
    var validMarkets: List<GoldMarketPrice> = emptyList()

    private var sortParams: SortGoldParams = SortGoldParams()
    private var filterParams: FilterGoldParams = FilterGoldParams()

    var appExchangesInfo: List<GoldExchangeInfo>? = null

    private val _priceState = MutableStateFlow<ViewState<List<GoldMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<GoldMarketPrice>>> get() = _priceState.asStateFlow()

    init {
        getExchanges()
        getPrices()
        startTimer()
    }

    fun getExchanges() {
        viewModelScope.launch {
            getAppExchangesUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {
                        appExchangesInfo = response.result
//                        getPrices()
                    }

                    is ApiCallResult.Failure -> {
                    }

                }

            }
        }
    }

    fun getPrices() {
        getExchanges()
        _timer.intValue = REFRESH_TIMER


        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        val userExchanges = getUserExchanges()
        filterParams.addFee = exchangesLocalDataSource.addFee()
        val mergedFlow = mutableListOf<Flow<ApiCallResult<GoldMarketPrice>>>()

        if (appExchangesInfo?.isNotEmpty() != true)
            mergedFlow.apply {
                add(getDigikalaPriceUseCase.action())
                add(getGoldikaPriceUseCase.action())
                add(getHamrahGoldPriceUseCase.action())
                add(getTlynPriceUseCase.action())
                add(getMelliGoldPriceUseCase.action())
                add(getTalaseaPriceUseCase.action())
                add(getWallGoldPriceUseCase.action())
                add(getMilliPriceUseCase.action())
                add(getTechnoGoldPriceUseCase.action())
                add(getDaricPriceUseCase.action("GOLD18TMN"))
            }
        else
            mergedFlow.apply {
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.digikala }?.active == true)
                    add(getDigikalaPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.goldika }?.active == true)
                    add(getGoldikaPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.taline }?.active == true)
                    add(getTlynPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.hamrahgold }?.active == true)
                    add(getHamrahGoldPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.melligold }?.active == true)
                    add(getMelliGoldPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.talasea }?.active == true)
                    add(getTalaseaPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.wallgold }?.active == true)
                    add(getWallGoldPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.milli }?.active == true)
                    add(getMilliPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.technoGold }?.active == true)
                    add(getTechnoGoldPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.daric }?.active == true)
                    add(getDaricPriceUseCase.action("GOLD18TMN"))
            }

        viewModelScope.launch {
            merge(*mergedFlow.toTypedArray())
//            mergedFlow
                .collect { response ->
                    when (response) {
                        is ApiCallResult.Success -> {

                            allMarkets =
                                allMarkets.filterNot { it.exchangeInfo.exchange == response.result.exchangeInfo.exchange }
                                    .toMutableList()

                            allMarkets.add(response.result)

                            allMarkets = removeInvalidExchangeUseCase.action(
                                RemoveInvalidGoldExchangesParams(
//                                    exchangesInfo = exchangesInfo,
                                    markets = allMarkets
                                )
                            ).toMutableList()

                            // get avg from all of exchanges, so bad price will be detected better.
                            // we do not use add exchanges in avg,
                            // if first exchange price was out of range, avg will be invalid and broke other prices
                            val (avgBuy, avgSell) = getMarketAvgUseCase.action(
                                allMarkets,
                                userExchanges
                            )


                            validMarkets = removeOutOfRangeExchangesUseCase.action(
                                RemoveOutOfRangeGoldExchangesParams(
                                    avgSell = avgSell,
                                    avgBuy = avgBuy,
                                    markets = allMarkets,
                                )
                            )

                            filterParams.markets = validMarkets
                            filterParams.userExchanges = userExchanges
                            filterParams.exchangesInfo = appExchangesInfo
                            validMarkets = filterMarketUseCase.action(filterParams)
                            sortParams.markets = validMarkets

                            _priceState.update { currentList ->

                                ViewState.Success(sortMarketUseCase.action(sortParams))

                            }
                        }

                        is ApiCallResult.Failure -> {
//                        _priceState.update { currentList ->
//                            currentList + ViewState.Failure(error = response.error)
//                        }
                        }

                    }
                }
        }
    }
//

    fun sort(sortField: SortField, sortOrder: SortOrder) {
        _priceState.update {
            sortParams.markets = validMarkets
            sortParams.sortField = sortField
            sortParams.sortOrder = sortOrder
            ViewState.Success(sortMarketUseCase.action(sortParams))
        }
    }

    fun filter() {
        _priceState.update {
            filterParams.markets = validMarkets
            ViewState.Success(filterMarketUseCase.action(filterParams))
        }
    }

    fun getMarketAverage(): Pair<Float, Float> {
        val userExchanges = getUserExchanges()
        return getMarketAvgUseCase.action(validMarkets, userExchanges)
    }

    fun saveAddFee(addFee: Boolean) {
        exchangesLocalDataSource.saveAddFee(addFee)
    }

    fun getAddFee(): Boolean {
        return exchangesLocalDataSource.addFee()
    }

    fun saveDisplayExchanges(exchanges: List<GoldExchanges>) {
        exchangesLocalDataSource.saveUserGoldExchanges(exchanges)
    }

    fun getUserExchanges(): List<GoldExchanges> {
        // if user exchanges was null, we show all exchanges from Exchanges enum
        return exchangesLocalDataSource.getUserGoldExchanges() ?: GoldExchanges.entries
    }

    fun getActiveExchangesInfo(): List<GoldExchangeInfo> {
        return exchangesLocalDataSource.getGoldExchangesInfo()?.filter {
            it.active && it.display
        } ?: emptyList()
    }

    private var isRunning = false
    private var timerJob: Job? = null
    fun startTimer() {
        if (isRunning) return
        isRunning = true
        timerJob = viewModelScope.launch {

            while (true) {
                _timer.intValue = _timer.intValue - 1
                delay(1000)
                if (_timer.intValue == 0) {
                    getPrices()
                }
            }
        }
    }

    fun stopTimer() {
        isRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        stopTimer()
    }

}