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
import com.numtory.application.features.gold.domain.usecase.GetDaricPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetDigikalaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetEcoGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGeramiPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldikaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetHamrahGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMelliGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetMilliPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetNoghreseaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTalaseaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTechnoGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetTlynPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetWallGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetZarafzaPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetZarminexPriceUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveInvalidGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangeUseCase
import com.numtory.application.features.gold.domain.usecase.RemoveOutOfRangeGoldExchangesParams
import com.numtory.application.features.gold.domain.usecase.SortGoldMarketUseCase
import com.numtory.application.features.gold.domain.usecase.SortGoldParams
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.ui.theme.GOLD
import com.numtory.application.ui.theme.REFRESH_TIMER
import com.numtory.application.ui.theme.SILVER
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
import java.util.Collections

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
    private val getEcoGoldPriceUseCase: GetEcoGoldPriceUseCase,
    private val getZarminexPriceUseCase: GetZarminexPriceUseCase,
    private val getNoghreseaPriceUseCase: GetNoghreseaPriceUseCase,
    private val getGeramiPriceUseCase: GetGeramiPriceUseCase,
    private val getZarafzaPriceUseCase: GetZarafzaPriceUseCase,

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
    var priceJob :Job? = null

    var allMarkets: MutableList<GoldMarketPrice> = mutableListOf()
    var validMarkets: List<GoldMarketPrice> = emptyList()

    var sortParams: SortGoldParams = SortGoldParams()
        private set
    var filterParams: FilterGoldParams = FilterGoldParams()
        private set

    var appExchangesInfo: List<GoldExchangeInfo>? = null
    var symbol: String = GOLD

    private val _priceState = MutableStateFlow<ViewState<List<GoldMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<GoldMarketPrice>>> get() = _priceState.asStateFlow()

    var selectedToken = mutableStateOf(GOLD)
        private set

    fun selectToken(token: String) {
        selectedToken.value = token
    }

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

    fun getPrices(symbol: String = this.symbol) {
        if (this.symbol != symbol) {
            allMarkets.clear()
            validMarkets = Collections.emptyList()
            priceJob?.cancel()
        }

        this.symbol = symbol
        getExchanges()
        _timer.intValue = REFRESH_TIMER


        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        val userExchanges = getUserExchanges()
        val mergedFlow = mutableListOf<Flow<ApiCallResult<GoldMarketPrice>>>()

        if (appExchangesInfo?.isNotEmpty() != true)
            mergedFlow.apply {
                if (symbol == GOLD) {
                    add(getDigikalaPriceUseCase.action())
                    add(getGoldikaPriceUseCase.action())
                    add(getHamrahGoldPriceUseCase.action())
                    add(getTlynPriceUseCase.action())
                    add(getMelliGoldPriceUseCase.action())
                    add(getTalaseaPriceUseCase.action())
                    add(getWallGoldPriceUseCase.action())
                    add(getMilliPriceUseCase.action())
                    add(getTechnoGoldPriceUseCase.action())
                    add(getZarminexPriceUseCase.action())
                    add(getDaricPriceUseCase.action("GOLD18TMN"))
                    add(getEcoGoldPriceUseCase.action("GOLD18-IRT"))
                    add(getGeramiPriceUseCase.action(GOLD))
                    add(getZarafzaPriceUseCase.action())
                }
                if (symbol == SILVER) {
                    add(getDaricPriceUseCase.action("SILVERTMN"))
                    add(getEcoGoldPriceUseCase.action("SILVER999-IRT"))
                    add(getNoghreseaPriceUseCase.action())
                    add(getGeramiPriceUseCase.action(SILVER))
                }
            }
        else
            mergedFlow.apply {
                if (symbol == GOLD) {
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
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.zarminex }?.active == true)
                        add(getZarminexPriceUseCase.action())
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.daric }?.active == true)
                        add(getDaricPriceUseCase.action("GOLD18TMN"))
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.ecogold }?.active == true)
                        add(getEcoGoldPriceUseCase.action("GOLD18-IRT"))
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.gerami }?.active == true)
                        add(getGeramiPriceUseCase.action(GOLD))
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.zarafza }?.active == true)
                        add(getZarafzaPriceUseCase.action())
                }

                if (symbol == SILVER) {
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.daric }?.active == true)
                        add(getDaricPriceUseCase.action("SILVERTMN"))
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.ecogold }?.active == true)
                        add(getEcoGoldPriceUseCase.action("SILVER999-IRT"))
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.noghresea }?.active == true)
                        add(getNoghreseaPriceUseCase.action())
                    if (appExchangesInfo?.firstOrNull { it.exchange == GoldExchanges.gerami }?.active == true)
                        add(getGeramiPriceUseCase.action(SILVER))
                }
            }

        priceJob = viewModelScope.launch {
            merge(*mergedFlow.toTypedArray())
//            mergedFlow
                .collect { response ->
                    when (response) {
                        is ApiCallResult.Success -> {

                            allMarkets =
                                allMarkets.filterNot { it.exchangeInfo.exchange == response.result.exchangeInfo.exchange }
                                    .toMutableList()

                            if (response.result.symbol?.lowercase()?.contains(symbol.lowercase()) == true)
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
                            emitData()
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
        sortParams.sortField = sortField
        sortParams.sortOrder = sortOrder
        emitData()

    }

    fun filter() {
        emitData()
    }

    fun emitData() {
        _priceState.update {
            //
            filterParams.markets = validMarkets
            filterParams.addFee = exchangesLocalDataSource.addFee()
            filterParams.userExchanges = getUserExchanges()
            filterParams.exchangesInfo = appExchangesInfo
            validMarkets = filterMarketUseCase.action(filterParams)
            //
            sortParams.markets = validMarkets
            validMarkets = sortMarketUseCase.action(sortParams)

            ViewState.Success(validMarkets)
        }
    }

    fun getMarketAverage(): Pair<Double, Double> {
        val userExchanges = getUserExchanges()
        return getMarketAvgUseCase.action(validMarkets, userExchanges)
    }

    fun saveAddFee(addFee: Boolean) {
        exchangesLocalDataSource.saveAddFee(addFee)
        filter()
    }

    fun getAddFee(): Boolean {
        return exchangesLocalDataSource.addFee()
    }

    fun saveDisplayExchanges(exchanges: List<GoldExchanges>) {
        exchangesLocalDataSource.saveUserGoldExchanges(exchanges)
        filter()
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