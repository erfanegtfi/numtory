package com.numtory.application.features.cryptoMarket.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.domain.usecase.GetCryptoGlobalMarketPricesUseCase
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
class CryptoGlobalMarketPriceViewModel
constructor(
    private val getCryptoGlobalMarketPricesUseCase: GetCryptoGlobalMarketPricesUseCase,

    ) : ViewModel() {

    private val _timer = mutableIntStateOf(REFRESH_TIMER)
    val timer: State<Int> get() = _timer


    var cryptoPrices: MutableList<CryptoMarketPrice> = mutableListOf()

    private val _priceState = MutableStateFlow<ViewState<List<CryptoMarketPrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<CryptoMarketPrice>>> get() = _priceState.asStateFlow()

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

                        val l =
                            cryptoPrices.filter { it.symbol?.uppercase() == "PAXG" || it.symbol?.uppercase() == "XAUT" || it.symbol?.uppercase() == "CL" || it.symbol?.uppercase() == "AUXT" || it.symbol?.uppercase() == "COPPER" }
                        cryptoPrices.removeAll { it.symbol?.uppercase() == "PAXG" || it.symbol?.uppercase() == "XAUT" || it.symbol?.uppercase() == "CL" || it.symbol?.uppercase() == "AUXT" || it.symbol?.uppercase() == "COPPER" || it.symbol?.uppercase() == "USDT" }
                        cryptoPrices.addAll(1, l)
                        _priceState.value = ViewState.Success(cryptoPrices)
                    }

                    is ApiCallResult.Failure -> {
//                        _priceState.value = ViewState.Failure(response.error)
                    }

                }

            }
        }
    }

    fun filter(searchQuery: String) {
        _priceState.value = ViewState.Success(cryptoPrices.filter {
            it.symbol?.lowercase()
                ?.contains(searchQuery.lowercase()) == true || it.name?.lowercase()
                ?.contains(searchQuery.lowercase()) == true
        })
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