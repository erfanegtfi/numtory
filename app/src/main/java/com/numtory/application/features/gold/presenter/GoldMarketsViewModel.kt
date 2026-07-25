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
import com.numtory.application.features.gold.domain.usecase.GetActiveGoldExchangesInfoUseCase
import com.numtory.application.features.gold.domain.usecase.GetAppGoldExchangesUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldBestPriceUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldMarketAvgUseCase
import com.numtory.application.features.gold.domain.usecase.GetGoldPriceFlowsUseCase
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
    private val getActiveExchangesInfoUseCase: GetActiveGoldExchangesInfoUseCase,
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
    private val getAppExchangesUseCase: GetAppGoldExchangesUseCase,
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
            allMarkets = emptyList()
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
            getPriceFlowsUseCase.action(symbol, appExchangesInfo).merge().collect { response ->
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

    fun getUserExchanges(): List<GoldExchanges> = getUserExchangesUseCase.action()

    fun getActiveExchangesInfo(): List<GoldExchangeInfo> = getActiveExchangesInfoUseCase.action()

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
        emitData()
    }

    private fun emitData() {
        validMarkets = prepareMarketListUseCase.action(
            PrepareGoldMarketListParams(
                markets = validMarkets,
                userExchanges = getUserExchanges(),
                exchangesInfo = appExchangesInfo,
                addFee = exchangesLocalDataSource.addFee(),
                sortField = sortParams.sortField,
                sortOrder = sortParams.sortOrder,
            )
        )

        _priceState.value = ViewState.Success(validMarkets)
    }

    // endregion

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        viewModelScope.cancel()
    }
}
