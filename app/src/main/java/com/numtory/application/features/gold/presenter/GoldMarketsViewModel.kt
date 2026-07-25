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
import com.numtory.application.features.gold.domain.usecase.GetGoldBestPriceUseCase
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
import com.numtory.application.features.market.domain.entities.BestPrices
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
import kotlinx.coroutines.launch

private const val DARIC_GOLD = "GOLD18TMN"
private const val DARIC_SILVER = "SILVERTMN"
private const val ECOGOLD_GOLD = "GOLD18-IRT"
private const val ECOGOLD_SILVER = "SILVER999-IRT"

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
    private val getBestPriceUseCase: GetGoldBestPriceUseCase,
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppGoldExchangesUseCase,
    private val removeInvalidExchangeUseCase: RemoveInvalidGoldExchangeUseCase,
) : ViewModel() {

    private class PriceSource(
        val exchange: GoldExchanges,
        val open: () -> Flow<ApiCallResult<GoldMarketPrice>>,
    )

    // region State

    private val _priceState = MutableStateFlow<ViewState<List<GoldMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<GoldMarketPrice>>> get() = _priceState.asStateFlow()

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    private val _selectedToken = mutableStateOf(GOLD)
    val selectedToken: State<String> get() = _selectedToken

    var sortParams: SortGoldParams = SortGoldParams()
        private set
    private var filterParams: FilterGoldParams = FilterGoldParams()

    private var allMarkets: MutableList<GoldMarketPrice> = mutableListOf()

    private var validMarkets: List<GoldMarketPrice> = emptyList()

    private var appExchangesInfo: List<GoldExchangeInfo>? = null
    private var symbol: String = GOLD

    private var priceJob: Job? = null
    private var timerJob: Job? = null

    init {
        getExchanges()
        getPrices()
        startTimer()
    }

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

    fun filter() {
        emitData()
    }

    fun getMarketAverage(): Pair<Double, Double> =
        getMarketAvgUseCase.action(validMarkets, getUserExchanges())

    /** Best buy and best sell over the rendered list, each with the exchange offering it. */
    fun getBestPrices(): BestPrices =
        getBestPriceUseCase.action(validMarkets)

    fun saveAddFee(addFee: Boolean) {
        exchangesLocalDataSource.saveAddFee(addFee)
        filter()
    }

    fun getAddFee(): Boolean = exchangesLocalDataSource.addFee()

    fun saveDisplayExchanges(exchanges: List<GoldExchanges>) {
        exchangesLocalDataSource.saveUserGoldExchanges(exchanges)
        filter()
    }

    fun getUserExchanges(): List<GoldExchanges> =
        exchangesLocalDataSource.getUserGoldExchanges() ?: GoldExchanges.entries

    fun getActiveExchangesInfo(): List<GoldExchangeInfo> =
        exchangesLocalDataSource.getGoldExchangesInfo()
            ?.filter { it.active && it.display }
            ?: emptyList()

    fun startTimer() {
        if (timerJob != null) return
        timerJob = viewModelScope.launch {
            while (true) {
                _timer.intValue = _timer.intValue - 1
                delay(1000)
                if (_timer.intValue == 0) getPrices()
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun getExchanges() {
        viewModelScope.launch {
            getAppExchangesUseCase.action().collect { response ->
                if (response is ApiCallResult.Success) appExchangesInfo = response.result
            }
        }
    }

    private fun priceSourcesFor(symbol: String): List<PriceSource> = when (symbol) {
        GOLD -> listOf(
            PriceSource(GoldExchanges.digikala) { getDigikalaPriceUseCase.action() },
            PriceSource(GoldExchanges.goldika) { getGoldikaPriceUseCase.action() },
            PriceSource(GoldExchanges.taline) { getTlynPriceUseCase.action() },
            PriceSource(GoldExchanges.hamrahgold) { getHamrahGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.melligold) { getMelliGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.talasea) { getTalaseaPriceUseCase.action() },
            PriceSource(GoldExchanges.wallgold) { getWallGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.milli) { getMilliPriceUseCase.action() },
            PriceSource(GoldExchanges.technoGold) { getTechnoGoldPriceUseCase.action() },
            PriceSource(GoldExchanges.zarminex) { getZarminexPriceUseCase.action() },
            PriceSource(GoldExchanges.daric) { getDaricPriceUseCase.action(DARIC_GOLD) },
            PriceSource(GoldExchanges.ecogold) { getEcoGoldPriceUseCase.action(ECOGOLD_GOLD) },
            PriceSource(GoldExchanges.gerami) { getGeramiPriceUseCase.action(GOLD) },
            PriceSource(GoldExchanges.zarafza) { getZarafzaPriceUseCase.action() },
        )

        SILVER -> listOf(
            PriceSource(GoldExchanges.daric) { getDaricPriceUseCase.action(DARIC_SILVER) },
            PriceSource(GoldExchanges.ecogold) { getEcoGoldPriceUseCase.action(ECOGOLD_SILVER) },
            PriceSource(GoldExchanges.noghresea) { getNoghreseaPriceUseCase.action() },
            PriceSource(GoldExchanges.gerami) { getGeramiPriceUseCase.action(SILVER) },
        )

        else -> emptyList()
    }

    /**
     * Until the app exchange config arrives we query every source; once it is known,
     * only the exchanges the backend marked active are queried.
     */
    private fun priceFlowsFor(symbol: String): List<Flow<ApiCallResult<GoldMarketPrice>>> {
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
        price: GoldMarketPrice,
        symbol: String,
        userExchanges: List<GoldExchanges>,
    ) {
        allMarkets = allMarkets
            .filterNot { it.exchangeInfo.exchange == price.exchangeInfo.exchange }
            .toMutableList()

        if (price.symbol?.lowercase()?.contains(symbol.lowercase()) == true)
            allMarkets.add(price)

        allMarkets = removeInvalidExchangeUseCase
            .action(RemoveInvalidGoldExchangesParams(markets = allMarkets))
            .toMutableList()

        // Average over every exchange, not just the user's, so a bad price stands out.
        // Adding exchanges into the average would let one out-of-range first price
        // skew it and drag the rest of the list out with it.
        val (avgBuy, avgSell) = getMarketAvgUseCase.action(allMarkets, userExchanges)

        validMarkets = removeOutOfRangeExchangesUseCase.action(
            RemoveOutOfRangeGoldExchangesParams(
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
