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
import com.numtory.application.features.gold.domain.usecase.GetGoldBestPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldExchangeCatalogUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldPriceFlowsUseCase
import com.numtory.application.features.gold.domain.usecase.GetSelectableGoldExchangesUseCase
import com.numtory.application.features.gold.domain.usecase.GetUserGoldExchangesUseCase
import com.numtory.application.features.gold.domain.usecase.MergeGoldPriceParams
import com.numtory.application.features.gold.domain.usecase.MergeGoldPriceUseCase
import com.numtory.application.features.gold.domain.usecase.PrepareGoldMarketListParams
import com.numtory.application.features.gold.domain.usecase.PrepareGoldMarketListUseCase
import com.numtory.application.features.gold.domain.usecase.SortGoldParams
import com.numtory.application.features.market.domain.entities.BestPrices
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.ui.theme.GOLD
import com.numtory.application.ui.theme.REFRESH_TIMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

/**
 * Drives the gold / silver price list.
 *
 * ### Three different "exchange" lists — do not mix them up
 * | What | Where it comes from | Used for |
 * |---|---|---|
 * | **catalog** ([exchangeCatalog]) | backend, cached locally | which exchanges to *poll* |
 * | **selectable** ([getSelectableExchanges]) | catalog filtered to `active && display` | rows in the settings sheet |
 * | **user** ([getUserExchanges]) | the user's own ticks | which exchanges to *show* |
 *
 * ### Three different market lists
 * Each incoming price walks through them in order:
 *  1. [allMarkets] — one entry per exchange, raw, latest price wins.
 *  2. [validMarkets] — [allMarkets] minus prices too far from the market average.
 *  3. [displayedMarkets] — [validMarkets] narrowed to the user's exchanges, then sorted.
 *     This is what [priceState] carries, and what the average / best-price helpers read.
 *
 * Steps 1–2 only change when a price arrives; step 3 also re-runs when the user changes
 * a setting, which is why it always rebuilds from [validMarkets] rather than from itself.
 */
@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class GoldMarketsViewModel
constructor(
    private val getPriceFlowsUseCase: GetGoldPriceFlowsUseCase,
    private val mergePriceUseCase: MergeGoldPriceUseCase,
    private val prepareMarketListUseCase: PrepareGoldMarketListUseCase,
    private val getMarketAvgUseCase: GetGoldMarketAvgUseCase,
    private val getBestPriceUseCase: GetGoldBestPriceUseCase,
    private val getUserExchangesUseCase: GetUserGoldExchangesUseCase,
    private val getSelectableExchangesUseCase: GetSelectableGoldExchangesUseCase,
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
    private val getExchangeCatalogUseCase: GetGoldExchangeCatalogUseCase,
) : ViewModel() {

    // region State

    private val _priceState = MutableStateFlow<ViewState<List<GoldMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<GoldMarketPrice>>> get() = _priceState.asStateFlow()

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    private val _selectedToken = mutableStateOf(GOLD)
    val selectedToken: State<String> get() = _selectedToken

    var sortParams: SortGoldParams = SortGoldParams()
        private set

    /** Latest price per exchange, exactly as received. */
    private var allMarkets: List<GoldMarketPrice> = emptyList()

    /** [allMarkets] with outliers dropped — the honest picture of the whole market. */
    private var validMarkets: List<GoldMarketPrice> = emptyList()

    /** [validMarkets] narrowed to the user's exchanges and sorted — what the screen renders. */
    private var displayedMarkets: List<GoldMarketPrice> = emptyList()

    /** Every exchange the backend knows about; `null` until the first fetch lands. */
    private var exchangeCatalog: List<GoldExchangeInfo>? = null

    /** The metal currently on screen: [GOLD] or `SILVER`. */
    private var symbol: String = GOLD

    private var priceJob: Job? = null
    private var timerJob: Job? = null

    init {
        getPrices()
        startTimer()
    }

    fun selectToken(token: String) {
        _selectedToken.value = token
    }

    /**
     * Fetches one round of prices: every exchange that quotes [symbol] is asked in
     * parallel and rows appear as each answers. Called on init, on every timer tick,
     * and when the user switches metal.
     */
    fun getPrices(symbol: String = this.symbol) {
        if (symbol != this.symbol) startOver(symbol)

        refreshExchangeCatalog()
        _timer.intValue = REFRESH_TIMER

        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        // Read once, so every price in this round is filtered against the same choice.
        val userExchanges = getUserExchanges()

        // Each source emits a single price and completes, so this job ends by itself.
        priceJob = viewModelScope.launch {
            getPriceFlowsUseCase.action(symbol, exchangeCatalog).merge().collect { response ->
                if (response is ApiCallResult.Success)
                    onPriceReceived(response.result, symbol, userExchanges)
                // Failures are ignored on purpose: a single exchange going down
                // must not replace the list that the other exchanges already filled.
            }
        }
    }

    /** Switching metal invalidates every price we hold — drop them and stop the old round. */
    private fun startOver(newSymbol: String) {
        priceJob?.cancel()
        allMarkets = emptyList()
        validMarkets = emptyList()
        displayedMarkets = emptyList()
        symbol = newSymbol
    }

    fun sort(sortField: SortField, sortOrder: SortOrder) {
        sortParams.sortField = sortField
        sortParams.sortOrder = sortOrder
        rebuildDisplayedMarkets()
    }

    fun getMarketAverage(): Pair<Double, Double> =
        getMarketAvgUseCase.action(displayedMarkets, getUserExchanges())

    /** Best buy and best sell over the rendered list, each with the exchange offering it. */
    fun getBestPrices(): BestPrices =
        getBestPriceUseCase.action(displayedMarkets)

    fun saveAddFee(addFee: Boolean) {
        exchangesLocalDataSource.saveAddFee(addFee)
        rebuildDisplayedMarkets()
    }

    fun getAddFee(): Boolean = exchangesLocalDataSource.addFee()

    fun saveDisplayExchanges(exchanges: List<GoldExchanges>) {
        exchangesLocalDataSource.saveUserGoldExchanges(exchanges)
        rebuildDisplayedMarkets()
    }

    /** The exchanges the user ticked; all of them when they have never chosen. */
    fun getUserExchanges(): List<GoldExchanges> = getUserExchangesUseCase.action()

    /** The exchanges worth offering in the settings sheet — see the class doc. */
    fun getSelectableExchanges(): List<GoldExchangeInfo> = getSelectableExchangesUseCase.action()

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

    /**
     * Refreshes [exchangeCatalog] in the background: the cached copy arrives first, the
     * network copy a moment later. The round of prices started alongside this call still
     * uses the previous catalog — deliberately, so a slow catalog request never delays
     * prices. A `null` catalog simply means "poll every exchange".
     */
    private fun refreshExchangeCatalog() {
        viewModelScope.launch {
            getExchangeCatalogUseCase.action().collect { response ->
                if (response is ApiCallResult.Success) exchangeCatalog = response.result
            }
        }
    }

    // endregion

    // region Pipeline

    /** Folds one exchange's price into [allMarkets] / [validMarkets], then repaints. */
    private fun onPriceReceived(
        price: GoldMarketPrice,
        symbol: String,
        userExchanges: List<GoldExchanges>,
    ) {
        val snapshot = mergePriceUseCase.action(
            MergeGoldPriceParams(
                price = price,
                symbol = symbol,
                markets = allMarkets,
                userExchanges = userExchanges,
            )
        )
        allMarkets = snapshot.allMarkets
        validMarkets = snapshot.validMarkets
        rebuildDisplayedMarkets()
    }

    /** Re-derives [displayedMarkets] from [validMarkets] and publishes it to the screen. */
    private fun rebuildDisplayedMarkets() {
        displayedMarkets = prepareMarketListUseCase.action(
            PrepareGoldMarketListParams(
                markets = validMarkets,
                userExchanges = getUserExchanges(),
                exchangesInfo = exchangeCatalog,
                addFee = exchangesLocalDataSource.addFee(),
                sortField = sortParams.sortField,
                sortOrder = sortParams.sortOrder,
            )
        )

        _priceState.value = ViewState.Success(displayedMarkets)
    }

    // endregion

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        viewModelScope.cancel()
    }
}
