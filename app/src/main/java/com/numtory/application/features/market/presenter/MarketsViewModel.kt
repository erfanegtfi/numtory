package com.numtory.application.features.market.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.features.market.data.local.LocalDataRepository
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.market.domain.entities.MarketPrice
import com.numtory.application.features.market.domain.enums.Exchanges
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder
import com.numtory.application.features.market.domain.usecase.FilterMarketUseCase
import com.numtory.application.features.market.domain.usecase.FilterParams
import com.numtory.application.features.market.domain.usecase.GetAbanTetherPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetArzplusPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBit24PriceUseCase
import com.numtory.application.features.market.domain.usecase.GetBitPinPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetCoinkadePriceUseCase
import com.numtory.application.features.market.domain.usecase.GetEterexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetMarketAvgUseCase
import com.numtory.application.features.market.domain.usecase.GetNobitexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPingiPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetPoolenoPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetSarmayexPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTabtealPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTetherLandPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetTwoxPriceUseCase
import com.numtory.application.features.market.domain.usecase.GetWallexPriceUseCase
import com.numtory.application.features.market.domain.usecase.RemoveOutOfRangeExchangesParams
import com.numtory.application.features.market.domain.usecase.RemoveOutOfRangeExchangeUseCase
import com.numtory.application.features.market.domain.usecase.SortMarketUseCase
import com.numtory.application.features.market.domain.usecase.SortParams
import com.numtory.application.ui.theme.REFRESH_TIMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

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
    private val sortMarketUseCase: SortMarketUseCase,
    private val filterMarketUseCase: FilterMarketUseCase,
    private val removeOutOfRangeExchangesUseCase: RemoveOutOfRangeExchangeUseCase,
    private val getMarketAvgUseCase: GetMarketAvgUseCase,
    private val localDataRepository: LocalDataRepository,
) : ViewModel() {

    private val _timer = mutableStateOf<Int>(REFRESH_TIMER)
    val timer: State<Int> get() = _timer

    var allMarkets: MutableList<MarketPrice> = mutableListOf()
    var validMarkets: List<MarketPrice> = emptyList()

    private var sortParams: SortParams = SortParams()
    private var filterParams: FilterParams = FilterParams()

    private val _priceState = MutableStateFlow<ViewState<List<MarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<MarketPrice>>> get() = _priceState.asStateFlow()

    fun getPrices() {
        _timer.value = REFRESH_TIMER


        if (_priceState.value is ViewState.Init)
            _priceState.value = ViewState.Loading

        val ae = getEnableExchanges()
        filterParams.addFee = localDataRepository.addFee()

        val mergedFlow = merge(
            getBitPinPriceUseCase.action(5),
            getTetherLandPriceUseCase.action(),
            getAbanTetherPriceUseCase.action("USDT"),
            getNobitexPriceUseCase.action("USDTIRT", "usdt-rls"),
            getTabtealPriceUseCase.action("IRT", "USDT"),
            getBit24PriceUseCase.action("IRT", "USDT"),
            getArzplusPriceUseCase.action("IRT", "USDT"),
            getTwoxPriceUseCase.action("IRT", "USDT"),
            getCoinkadePriceUseCase.action(),
            getPoolenoPriceUseCase.action("USDT", "TMN"),
            getEterexPriceUseCase.action("USDT"),
            getSarmayexPriceUseCase.action("USDT", "USDT_IRT"),
            getPingiPriceUseCase.action("USDT_IRT"),
            getWallexPriceUseCase.action("USDT","TMN"),
        )

        viewModelScope.launch {
            mergedFlow.collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {

                        allMarkets =
                            allMarkets.filterNot { it.exchange == response.result.exchange }
                                .toMutableList()


                        // get avg from all of exchanges, so bad price will be detected better.
                        // if we do not use add exchanges in avg,
                        // if first exchange price was out of range, avg will be invalid and broke other prices
                        val (avgBuy, avgSell) = getMarketAvgUseCase.action(allMarkets)
                        allMarkets.add(response.result) // its better to be after  getting avg

                        validMarkets = removeOutOfRangeExchangesUseCase.action(
                            RemoveOutOfRangeExchangesParams(
                                avgSell = avgSell,
                                avgBuy = avgBuy,
                                markets = allMarkets,
                            )
                        )

                        filterParams.markets = validMarkets
                        filterParams.activeExchanges = ae
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
        return getMarketAvgUseCase.action(validMarkets)
    }

    fun saveAddFee(addFee: Boolean) {
        localDataRepository.saveAddFee(addFee)
    }
    fun getAddFee(): Boolean {
        return  localDataRepository.addFee()
    }

    fun saveExchanges(exchanges: List<Exchanges>) {
        val json = Json.encodeToString(exchanges)
        localDataRepository.saveEnableExchanges(json)
    }

    fun getEnableExchanges(): List<Exchanges> {
        return localDataRepository.getEnableExchanges() ?: Exchanges.entries
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