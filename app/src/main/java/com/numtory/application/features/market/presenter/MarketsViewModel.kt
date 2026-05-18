package com.numtory.application.features.market.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
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
class MarketsViewModel
constructor(
    private val getBitPinPriceUseCase: GetBitPinPriceUseCase,
    private val getTetherLandPriceUseCase: GetTetherLandPriceUseCase,
    private val getAbanTetherPriceUseCase: GetAbanTetherPriceUseCase,
    private val getNobitexPriceUseCase: GetNobitexPriceUseCase,
    private val getTabtealPriceUseCase: GetTabtealPriceUseCase,
    private val getBit24PriceUseCase: GetBit24PriceUseCase,
    private val getArzplusPriceUseCase: GetArzplusPriceUseCase,
    private val getTwoxPriceUseCase: GetTwoxPriceUseCase,
    private val getCoinkadePriceUseCase: GetCoinkadePriceUseCase,
    private val getPoolenoPriceUseCase: GetPoolenoPriceUseCase,
    private val getEterexPriceUseCase: GetEterexPriceUseCase,
    private val getSarmayexPriceUseCase: GetSarmayexPriceUseCase,
    private val getPingiPriceUseCase: GetPingiPriceUseCase,
    private val getWallexPriceUseCase: GetWallexPriceUseCase,
    private val getSarafPriceUseCase: GetSarafPriceUseCase,
    private val getUbitexPriceUseCase: GetUbitexPriceUseCase,
    private val getRamzinexPriceUseCase: GetRamzinexPriceUseCase,
    private val getArzinjaPriceUseCase: GetArzinjaPriceUseCase,
    private val sortMarketUseCase: SortMarketUseCase,
    private val filterMarketUseCase: FilterMarketUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeExchangeUseCase,
    private val getMarketAvgUseCase: GetMarketAvgUseCase,
    private val exchangesLocalDataSource: ExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppExchangesUseCase,
    private val removeInvalidExchangeUseCase: RemoveInvalidExchangeUseCase,
) : ViewModel() {

    private val _timer = mutableStateOf<Int>(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    var allMarkets: MutableList<MarketPrice> = mutableListOf()
    var validMarkets: List<MarketPrice> = emptyList()

    private var sortParams: SortParams = SortParams()
    private var filterParams: FilterParams = FilterParams()

    var appExchangesInfo: List<ExchangeInfo>? = null

    private val _priceState = MutableStateFlow<ViewState<List<MarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<MarketPrice>>> get() = _priceState.asStateFlow()

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
        val mergedFlow = mutableListOf<Flow<ApiCallResult<MarketPrice>>>()

        if (appExchangesInfo?.isNotEmpty() != true)
            mergedFlow.apply {
                add(getBitPinPriceUseCase.action(5))
                add(getTetherLandPriceUseCase.action())
                add(getNobitexPriceUseCase.action("USDTIRT", "usdt-rls"))
                add(getTabtealPriceUseCase.action("IRT", "USDT"))
                add(getBit24PriceUseCase.action("IRT", "USDT"))
                add(getArzplusPriceUseCase.action("IRT", "USDT"))
                add(getTwoxPriceUseCase.action("IRT", "USDT"))
                add(getCoinkadePriceUseCase.action())
                add(getPoolenoPriceUseCase.action("USDT", "TMN"))
                add(getEterexPriceUseCase.action("USDT"))
                add(getPingiPriceUseCase.action("USDT_IRT"))
                add(getWallexPriceUseCase.action("USDT", "TMN"))
                add(getAbanTetherPriceUseCase.action("USDT"))
                add(getSarmayexPriceUseCase.action("USDT", "USDT_IRT"))
                add(getSarafPriceUseCase.action("USDT"))
                add(getUbitexPriceUseCase.action("USDT", "TMN"))
                add(getArzinjaPriceUseCase.action("USDT", "IRT"))
                add(getRamzinexPriceUseCase.action("2", "9"))
            }
//         mergedFlow = merge(
//            getBitPinPriceUseCase.action(5),
//            getTetherLandPriceUseCase.action(),
//            getAbanTetherPriceUseCase.action("USDT"),
//            getNobitexPriceUseCase.action("USDTIRT", "usdt-rls"),
//            getTabtealPriceUseCase.action("IRT", "USDT"),
//            getBit24PriceUseCase.action("IRT", "USDT"),
//            getArzplusPriceUseCase.action("IRT", "USDT"),
//            getTwoxPriceUseCase.action("IRT", "USDT"),
//            getCoinkadePriceUseCase.action(),
//            getPoolenoPriceUseCase.action("USDT", "TMN"),
//            getEterexPriceUseCase.action("USDT"),
//            getSarmayexPriceUseCase.action("USDT", "USDT_IRT"),
//            getPingiPriceUseCase.action("USDT_IRT"),
//            getWallexPriceUseCase.action("USDT", "TMN"),
//            getSarafPriceUseCase.action("USDT"),
//            getUbitexPriceUseCase.action("USDT", "TMN"),
//            getRamzinexPriceUseCase.action("2", "9"),
//        )
        else
            mergedFlow.apply {
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.bitpin }?.active == true)
                    add(getBitPinPriceUseCase.action(5))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.tetherland }?.active == true)
                    add(getTetherLandPriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitex }?.active == true)
                    add(getNobitexPriceUseCase.action("USDTIRT", "usdt-rls"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdeal }?.active == true)
                    add(getTabtealPriceUseCase.action("IRT", "USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.bit24 }?.active == true)
                    add(getBit24PriceUseCase.action("IRT", "USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.arzplus }?.active == true)
                    add(getArzplusPriceUseCase.action("IRT", "USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.twox }?.active == true)
                    add(getTwoxPriceUseCase.action("IRT", "USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.coinkade }?.active == true)
                    add(getCoinkadePriceUseCase.action())
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.pooleno }?.active == true)
                    add(getPoolenoPriceUseCase.action("USDT", "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.eterex }?.active == true)
                    add(getEterexPriceUseCase.action("USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.pingi }?.active == true)
                    add(getPingiPriceUseCase.action("USDT_IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.wallex }?.active == true)
                    add(getWallexPriceUseCase.action("USDT", "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.abantether }?.active == true)
                    add(getAbanTetherPriceUseCase.action("USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.sarmayex }?.active == true)
                    add(getSarmayexPriceUseCase.action("USDT", "USDT_IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.saraf }?.active == true)
                    add(getSarafPriceUseCase.action("USDT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.ubitex }?.active == true)
                    add(getUbitexPriceUseCase.action("USDT", "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.arzinja }?.active == true)
                    add(getArzinjaPriceUseCase.action("USDT", "IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.ramzinex }?.active == true)
                    add(getRamzinexPriceUseCase.action("2", "9"))
            }

        viewModelScope.launch {
            merge(*mergedFlow.toTypedArray())
//            mergedFlow
                .collect { response ->
                    when (response) {
                        is ApiCallResult.Success -> {

                            allMarkets =
                                allMarkets.filterNot { it.exchangeInfo.exchange == response.result.exchangeInfo?.exchange }
                                    .toMutableList()

                            allMarkets.add(response.result)

                            allMarkets = removeInvalidExchangeUseCase.action(
                                RemoveInvalidExchangesParams(
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
                                RemoveOutOfRangeExchangesParams(
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

    fun saveDisplayExchanges(exchanges: List<Exchanges>) {
        exchangesLocalDataSource.saveUserExchanges(exchanges)
    }

    fun getUserExchanges(): List<Exchanges> {
        // if user exchanges was null, we show all exchanges from Exchanges enum
        return exchangesLocalDataSource.getUserExchanges() ?: Exchanges.entries
    }

    fun getActiveExchangesInfo(): List<ExchangeInfo> {
        return exchangesLocalDataSource.getExchangesInfo()?.filter {
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