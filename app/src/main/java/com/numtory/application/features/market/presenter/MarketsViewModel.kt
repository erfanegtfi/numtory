package com.numtory.application.features.market.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.domain.entities.BestPrices
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
import com.numtory.application.features.market.domain.usecase.GetBitbargPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetExonyxPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetAsacoinePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBestPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetMorbitPriceUseCase
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
import kotlinx.coroutines.launch

private const val IRT = "IRT"
private const val TMN = "TMN"
private const val TOMAN = "TOMAN"
private const val RLS = "rls"
private const val IRR = "irr"

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
    private val getBitbargPriceUseCase: GetBitbargPriceUseCase,
    private val getArzinjaPriceUseCase: GetArzinjaPriceUseCase,
    private val getExonyxPriceUseCase: GetExonyxPriceUseCase,
    private val getMorbitPriceUseCase: GetMorbitPriceUseCase,
    private val getAsacoinePriceUseCase: GetAsacoinePriceUseCase,
    private val sortMarketUseCase: SortMarketUseCase,
    private val filterMarketUseCase: FilterMarketUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeExchangeUseCase,
    private val getMarketAvgUseCase: GetMarketAvgUseCase,
    private val getBestPriceUseCase: GetBestPriceUseCase,
    private val exchangesLocalDataSource: ExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppExchangesUseCase,
    private val removeInvalidExchangeUseCase: RemoveInvalidExchangeUseCase,
) : ViewModel() {

    private class PriceSource(
        val exchange: Exchanges,
        val open: () -> Flow<ApiCallResult<MarketPrice>>,
    )

    // region State

    private val _priceState = MutableStateFlow<ViewState<List<MarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<MarketPrice>>> get() = _priceState.asStateFlow()

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    private val _selectedToken = mutableStateOf(DEFAULT_TOKEN)
    val selectedToken: State<String> get() = _selectedToken

    var sortParams: SortParams = SortParams()
        private set
    private var filterParams: FilterParams = FilterParams()

    private var allMarkets: MutableList<MarketPrice> = mutableListOf()

    private var validMarkets: List<MarketPrice> = emptyList()

    private var appExchangesInfo: List<ExchangeInfo>? = null
    private var symbol: String = DEFAULT_TOKEN

    private var priceJob: Job? = null
    private var timerJob: Job? = null

    // endregion

    init {
        getExchanges()
        getPrices(symbol)
        startTimer()
    }

    // region Public API

    fun selectToken(token: String) {
        _selectedToken.value = token
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
        priceJob = viewModelScope.launch {
            priceFlowsFor(symbol).merge().collect { response ->
                if (response is ApiCallResult.Success)
                    onPriceReceived(response.result, symbol, userExchanges)
                // Failures are ignored on purpose: a single exchange going down
                // must not replace the list that the other exchanges already filled.
            }
        }
    }

    fun sort(sortField: SortField, sortOrder: SortOrder) {
        sortParams.sortField = sortField
        sortParams.sortOrder = sortOrder
        emitData()
    }

    private fun filter() {
        emitData()
    }

    fun getMarketAverage(): Pair<Double, Double> =
        getMarketAvgUseCase.action(validMarkets, getUserExchanges())

    fun getBestPrices(): BestPrices =
        getBestPriceUseCase.action(validMarkets)

    fun saveAddFee(addFee: Boolean) {
        exchangesLocalDataSource.saveAddFee(addFee)
        filter()
    }

    fun getAddFee(): Boolean = exchangesLocalDataSource.addFee()

    fun saveDisplayExchanges(exchanges: List<Exchanges>) {
        exchangesLocalDataSource.saveUserExchanges(exchanges)
        filter()
    }

    fun getUserExchanges(): List<Exchanges> =
        exchangesLocalDataSource.getUserExchanges() ?: Exchanges.entries

    fun getActiveExchangesInfo(): List<ExchangeInfo> =
        exchangesLocalDataSource.getExchangesInfo()
            ?.filter { it.active && it.display }
            ?: emptyList()

    fun startTimer() {
        if (timerJob != null) return
        timerJob = viewModelScope.launch {
            while (true) {
                _timer.intValue = _timer.intValue - 1
                delay(1000)
                if (_timer.intValue == 0) getPrices(symbol)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    // endregion

    // region Price sources

    private fun getExchanges() {
        viewModelScope.launch {
            getAppExchangesUseCase.action().collect { response ->
                if (response is ApiCallResult.Success) appExchangesInfo = response.result
            }
        }
    }

    private fun priceSourcesFor(symbol: String): List<PriceSource> = listOf(
        PriceSource(Exchanges.bitpin) { getBitPinPriceUseCase.action(symbol, IRT) },
        PriceSource(Exchanges.tetherland) { getTetherLandPriceUseCase.action(symbol) },
        PriceSource(Exchanges.nobitex) { getNobitexPriceUseCase.action(symbol, IRT, RLS) },
        PriceSource(Exchanges.tabdeal) { getTabtealPriceUseCase.action(IRT, symbol) },
        PriceSource(Exchanges.bit24) { getBit24PriceUseCase.action(IRT, symbol) },
        PriceSource(Exchanges.arzplus) { getArzplusPriceUseCase.action(IRT, symbol) },
        PriceSource(Exchanges.twox) { getTwoxPriceUseCase.action(IRT, symbol) },
        PriceSource(Exchanges.coinkade) { getCoinkadePriceUseCase.action(symbol) },
        PriceSource(Exchanges.pooleno) { getPoolenoPriceUseCase.action(symbol, TMN) },
        PriceSource(Exchanges.eterex) { getEterexPriceUseCase.action(symbol) },
        PriceSource(Exchanges.pingi) { getPingiPriceUseCase.action(symbol, IRT) },
        PriceSource(Exchanges.wallex) { getWallexPriceUseCase.action(symbol, TMN) },
        PriceSource(Exchanges.abantether) { getAbanTetherPriceUseCase.action(symbol) },
        PriceSource(Exchanges.sarmayex) { getSarmayexPriceUseCase.action(symbol, IRT) },
        PriceSource(Exchanges.saraf) { getSarafPriceUseCase.action(symbol) },
        PriceSource(Exchanges.ubitex) { getUbitexPriceUseCase.action(symbol, TMN) },
        PriceSource(Exchanges.arzinja) { getArzinjaPriceUseCase.action(symbol, IRT) },
        PriceSource(Exchanges.ramzinex) { getRamzinexPriceUseCase.action(symbol, IRR) },
        PriceSource(Exchanges.bitbarg) { getBitbargPriceUseCase.action(symbol) },
        PriceSource(Exchanges.arzypto) { getArzyptoPriceUseCase.action(TOMAN, symbol) },
        PriceSource(Exchanges.exonyx) { getExonyxPriceUseCase.action(symbol) },
        PriceSource(Exchanges.morbit) { getMorbitPriceUseCase.action(symbol, IRT) },
        PriceSource(Exchanges.asacoine) { getAsacoinePriceUseCase.action(symbol, TMN) },
    )

    /**
     * Until the app exchange config arrives we query every source; once it is known,
     * only the exchanges the backend marked active are queried.
     */
    private fun priceFlowsFor(symbol: String): List<Flow<ApiCallResult<MarketPrice>>> {
        val exchangesInfo = appExchangesInfo
        return priceSourcesFor(symbol)
            .filter { source ->
                exchangesInfo.isNullOrEmpty() ||
                        exchangesInfo.firstOrNull { it.exchange == source.exchange }?.active == true
            }
            .map { it.open() }
    }

    // endregion

    // region Pipeline

    private fun onPriceReceived(
        price: MarketPrice,
        symbol: String,
        userExchanges: List<Exchanges>,
    ) {
        allMarkets = allMarkets
            .filterNot { it.exchangeInfo.exchange == price.exchangeInfo.exchange }
            .toMutableList()

        if (price.symbol?.lowercase() == symbol.lowercase())
            allMarkets.add(price)

        allMarkets = removeInvalidExchangeUseCase
            .action(RemoveInvalidExchangesParams(markets = allMarkets))
            .toMutableList()

        // Average over every exchange, not just the user's, so a bad price stands out.
        // Adding exchanges into the average would let one out-of-range first price
        // skew it and drag the rest of the list out with it.
        val (avgBuy, avgSell) = getMarketAvgUseCase.action(allMarkets, userExchanges)

        validMarkets = removeOutOfRangeExchangesUseCase.action(
            RemoveOutOfRangeExchangesParams(
                avgSell = avgSell,
                avgBuy = avgBuy,
                markets = allMarkets,
            )
        )
        emitData()
    }

    private fun emitData() {
        filterParams.markets = validMarkets
        filterParams.addFee = exchangesLocalDataSource.addFee()
        filterParams.userExchanges = getUserExchanges()
        filterParams.exchangesInfo = appExchangesInfo
        validMarkets = filterMarketUseCase.action(filterParams)

        sortParams.markets = validMarkets
        validMarkets = sortMarketUseCase.action(sortParams)

        _priceState.value = ViewState.Success(validMarkets)
    }

    // endregion

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        viewModelScope.cancel()
    }
}
