package com.numtory.application.features.cryptoMarket.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.domain.entities.LabelType
import com.numtory.application.features.cryptoMarket.domain.usecase.FilterCryptoPricesUseCase
import com.numtory.application.features.cryptoMarket.domain.usecase.GetCryptoGlobalMarketPricesUseCase
import com.numtory.application.ui.theme.REFRESH_TIMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class CryptoGlobalMarketPriceViewModel
constructor(
    private val getCryptoGlobalMarketPricesUseCase: GetCryptoGlobalMarketPricesUseCase,
    private val filterCryptoPricesUseCase: FilterCryptoPricesUseCase,

    ) : ViewModel() {

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer


    var cryptoPrices: MutableList<CryptoMarketPrice> = mutableListOf()

    private val _priceState = MutableStateFlow<ViewState<List<CryptoMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<CryptoMarketPrice>>> get() = _priceState.asStateFlow()
    var searchQuery: String? = null
    var label: LabelType? = null

    init {
        getPrices()
        startTimer()
    }

    fun getPrices() {
        if (cryptoPrices.isEmpty())
            _priceState.value = ViewState.Loading
        _timer.intValue = REFRESH_TIMER
        viewModelScope.launch {
            getCryptoGlobalMarketPricesUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {
//                        cryptoPrices =  response.result.filter { it.symbolUSDT?.contains("_USDT") == true }
                        cryptoPrices.clear()
                        cryptoPrices.addAll(
                            response.result //.filter { it.symbolUSDT?.contains("_USDT") == true }
                        )

//                        val l =
//                            cryptoPrices.filter { it.symbol?.uppercase() == "PAXG" || it.symbol?.uppercase() == "XAUT" || it.symbol?.uppercase() == "CL" || it.symbol?.uppercase() == "AUXT" || it.symbol?.uppercase() == "COPPER" }
//                        cryptoPrices.removeAll { it.symbol?.uppercase() == "PAXG" || it.symbol?.uppercase() == "XAUT" || it.symbol?.uppercase() == "CL" || it.symbol?.uppercase() == "AUXT" || it.symbol?.uppercase() == "COPPER" || it.symbol?.uppercase() == "USDT" }
//                        cryptoPrices.addAll(1, l)
                        emitData()
//                        _priceState.value = ViewState.Success(cryptoPrices)
                    }

                    is ApiCallResult.Failure -> {
//                        _priceState.value = ViewState.Failure(response.error)
                    }

                }

            }
        }
    }


    fun filter(label: LabelType? = null) {
        this.label = label
        emitData()
    }

    fun filter(searchQuery: String? = null) {
        this.searchQuery = searchQuery
        emitData()
    }

    fun emitData() {
        _priceState.value =
            ViewState.Success(filterCryptoPricesUseCase(cryptoPrices, searchQuery, label))
    }

    private var isRunning = false
    private var timerJob: Job? = null
    fun startTimer() {
        if (isRunning) return
        isRunning = true
        timerJob = viewModelScope.launch {

            while (true) {
                _timer.intValue = _timer.intValue - 1
                delay(1000.milliseconds)
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