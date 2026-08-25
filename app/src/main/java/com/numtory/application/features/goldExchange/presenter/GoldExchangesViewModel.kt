package com.numtory.application.features.goldExchange.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo
import com.numtory.application.features.goldExchange.domain.entities.GoldMarketPrice
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldBestPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldExchangeCatalogUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetGoldPriceFlowsUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetSelectableGoldExchangesUseCase
import com.numtory.application.features.goldExchange.domain.usecase.GetUserGoldExchangesUseCase
import com.numtory.application.features.goldExchange.domain.usecase.MergeGoldPriceParams
import com.numtory.application.features.goldExchange.domain.usecase.MergeGoldPriceUseCase
import com.numtory.application.features.goldExchange.domain.usecase.PrepareGoldMarketListParams
import com.numtory.application.features.goldExchange.domain.usecase.PrepareGoldMarketListUseCase
import com.numtory.application.features.goldExchange.domain.usecase.SortGoldParams
import com.numtory.application.features.cryptoExchange.domain.entities.BestPrices
import com.numtory.application.features.cryptoExchange.domain.enums.SortField
import com.numtory.application.features.cryptoExchange.domain.enums.SortOrder
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


@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class GoldExchangesViewModel
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

    private var allMarkets: List<GoldMarketPrice> = emptyList()

    private var validMarkets: List<GoldMarketPrice> = emptyList()

    private var displayedMarkets: List<GoldMarketPrice> = emptyList()

    private var exchangeCatalog: List<GoldExchangeInfo>? = null

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

    fun getUserExchanges(): List<GoldExchanges> = getUserExchangesUseCase.action()

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

    private fun refreshExchangeCatalog() {
        viewModelScope.launch {
            getExchangeCatalogUseCase.action().collect { response ->
                if (response is ApiCallResult.Success) exchangeCatalog = response.result
            }
        }
    }

    // endregion

    // region Pipeline

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
