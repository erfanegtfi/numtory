package com.numtory.application.features.market.presenter;

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.numtory.application.features.market.domain.usecase.GetArzyptoPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBit24PriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBitPinPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetExonyxPriceUseCase
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
import com.numtory.application.ui.theme.DEFAULT_TOKEN
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
    private val getArzyptoPriceUseCase: GetArzyptoPriceUseCase,
    private val getEterexPriceUseCase: GetEterexPriceUseCase,
    private val getSarmayexPriceUseCase: GetSarmayexPriceUseCase,
    private val getPingiPriceUseCase: GetPingiPriceUseCase,
    private val getWallexPriceUseCase: GetWallexPriceUseCase,
    private val getSarafPriceUseCase: GetSarafPriceUseCase,
    private val getUbitexPriceUseCase: GetUbitexPriceUseCase,
    private val getRamzinexPriceUseCase: GetRamzinexPriceUseCase,
    private val getArzinjaPriceUseCase: GetArzinjaPriceUseCase,
    private val getExonyxPriceUseCase: GetExonyxPriceUseCase,
    private val sortMarketUseCase: SortMarketUseCase,
    private val filterMarketUseCase: FilterMarketUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeExchangeUseCase,
    private val getMarketAvgUseCase: GetMarketAvgUseCase,
    private val exchangesLocalDataSource: ExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppExchangesUseCase,
    private val removeInvalidExchangeUseCase: RemoveInvalidExchangeUseCase,
) : ViewModel() {

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    var allMarkets: MutableList<MarketPrice> = mutableListOf()
    var validMarkets: List<MarketPrice> = emptyList()

    private var sortParams: SortParams = SortParams()
    private var filterParams: FilterParams = FilterParams()

    var appExchangesInfo: List<ExchangeInfo>? = null

    private val _priceState = MutableStateFlow<ViewState<List<MarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<MarketPrice>>> get() = _priceState.asStateFlow()

    var symbol: String = DEFAULT_TOKEN
    var priceJob :Job? = null

    var selectedToken = mutableStateOf(DEFAULT_TOKEN)
        private set

    fun selectToken(token: String) {
        selectedToken.value = token
    }

    init {
        getExchanges()
        getPrices(symbol)
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
            validMarkets = emptyList()
            priceJob?.cancel()
        }

        this.symbol = symbol
        getExchanges()
        _timer.intValue = REFRESH_TIMER


        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        val userExchanges = getUserExchanges()
        filterParams.addFee = exchangesLocalDataSource.addFee()
        val mergedFlow = mutableListOf<Flow<ApiCallResult<MarketPrice>>>()

        if (appExchangesInfo?.isNotEmpty() != true)
            mergedFlow.apply {
                add(getBitPinPriceUseCase.action(symbol, "IRT"))
                add(getTetherLandPriceUseCase.action(symbol))
                add(getNobitexPriceUseCase.action(symbol, "IRT", "rls"))
                add(getTabtealPriceUseCase.action("IRT", symbol))
                add(getBit24PriceUseCase.action("IRT", symbol))
                add(getArzplusPriceUseCase.action("IRT", symbol))
                add(getTwoxPriceUseCase.action("IRT", symbol))
                add(getCoinkadePriceUseCase.action(symbol))
                add(getPoolenoPriceUseCase.action(symbol, "TMN"))
                add(getEterexPriceUseCase.action(symbol))
                add(getPingiPriceUseCase.action(symbol, "IRT"))
                add(getWallexPriceUseCase.action(symbol, "TMN"))
                add(getAbanTetherPriceUseCase.action(symbol))
                add(getSarmayexPriceUseCase.action(symbol, "IRT"))
                add(getSarafPriceUseCase.action(symbol))
                add(getUbitexPriceUseCase.action(symbol, "TMN"))
                add(getArzinjaPriceUseCase.action(symbol, "IRT"))
                add(getRamzinexPriceUseCase.action(symbol, "irr"))
                add(getArzyptoPriceUseCase.action("TOMAN", symbol))
                add(getExonyxPriceUseCase.action(symbol))
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
                    add(getBitPinPriceUseCase.action(symbol, "IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.tetherland }?.active == true)
                    add(getTetherLandPriceUseCase.action(symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.nobitex }?.active == true)
                    add(getNobitexPriceUseCase.action(symbol, "IRT", "rls"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdeal }?.active == true)
                    add(getTabtealPriceUseCase.action("IRT", symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.bit24 }?.active == true)
                    add(getBit24PriceUseCase.action("IRT", symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.arzplus }?.active == true)
                    add(getArzplusPriceUseCase.action("IRT", symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.twox }?.active == true)
                    add(getTwoxPriceUseCase.action("IRT", symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.coinkade }?.active == true)
                    add(getCoinkadePriceUseCase.action(symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.pooleno }?.active == true)
                    add(getPoolenoPriceUseCase.action(symbol, "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.eterex }?.active == true)
                    add(getEterexPriceUseCase.action(symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.pingi }?.active == true)
                    add(getPingiPriceUseCase.action(symbol, "IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.wallex }?.active == true)
                    add(getWallexPriceUseCase.action(symbol, "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.abantether }?.active == true)
                    add(getAbanTetherPriceUseCase.action(symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.sarmayex }?.active == true)
                    add(getSarmayexPriceUseCase.action(symbol, "IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.saraf }?.active == true)
                    add(getSarafPriceUseCase.action(symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.ubitex }?.active == true)
                    add(getUbitexPriceUseCase.action(symbol, "TMN"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.arzinja }?.active == true)
                    add(getArzinjaPriceUseCase.action(symbol, "IRT"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.ramzinex }?.active == true)
                    add(getRamzinexPriceUseCase.action(symbol, "irr"))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.arzypto }?.active == true)
                    add(getArzyptoPriceUseCase.action("TOMAN", symbol))
                if (appExchangesInfo?.firstOrNull { it.exchange == Exchanges.exonyx }?.active == true)
                    add(getExonyxPriceUseCase.action( symbol))
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

                            if (response.result.symbol?.lowercase() == symbol.lowercase())
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

    fun getMarketAverage(): Pair<Double, Double> {
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
                _timer.intValue = _timer.intValue - 1
                delay(1000)
                if (_timer.intValue == 0) {
                    getPrices(symbol)
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