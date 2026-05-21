package com.numtory.application.features.gold.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.numtory.application.features.gold.domain.usecase.GetDigikalaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldikaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.SortGoldMarketUseCase
import com.numtory.application.features.gold.domain.usecase.SortGoldParams
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.features.market.domain.usecase.FilterMarketUseCase
import com.numtory.application.features.market.domain.usecase.FilterParams
import com.numtory.application.features.market.domain.usecase.GetAbanTetherPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetAppExchangesUseCase
import com.numtory.application.features.market.domain.usecase.GetArzinjaPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetArzplusPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBit24PriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBitPinPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetMarketAvgUseCase
import com.numtory.application.features.market.domain.usecase.GetNobitexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPingiPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPoolenoPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetRamzinexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetSarafPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetSarmayexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTabtealPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTetherLandPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTwoxPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetUbitexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetWallexPriceUseCase
import com.numtory.application.features.market.domain.usecase.RemoveInvalidExchangeUseCase
import com.numtory.application.features.market.domain.usecase.RemoveInvalidExchangesParams
import com.numtory.application.features.market.domain.usecase.RemoveOutOfRangeExchangesParams
import com.numtory.application.features.market.domain.usecase.RemoveOutOfRangeExchangeUseCase
import com.numtory.application.features.market.domain.usecase.SortMarketUseCase
import com.numtory.application.features.market.domain.usecase.SortParams
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
        _timer.value = REFRESH_TIMER


        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        val userExchanges = getUserExchanges()
        filterParams.addFee = exchangesLocalDataSource.addFee()
        val mergedFlow = mutableListOf<Flow<ApiCallResult<GoldMarketPrice>>>()

        if (appExchangesInfo?.isNotEmpty() != true)
            mergedFlow.apply {
                add(getDigikalaPriceUseCase.action())
                add(getGoldikaPriceUseCase.action())

            }
        else
            mergedFlow.apply {
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.digikala }?.active == true)
                    add(getDigikalaPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.goldika }?.active == true)
                    add(getGoldikaPriceUseCase.action())


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
                _timer.value = _timer.value - 1
                delay(1000)
                if (_timer.value == 0) {
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