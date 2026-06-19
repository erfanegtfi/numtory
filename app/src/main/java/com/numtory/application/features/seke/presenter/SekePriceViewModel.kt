package com.numtory.application.features.seke.presenter;

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.domain.entities.SekePrice
import com.numtory.application.features.seke.domain.usecase.GetSekePricesUseCase
import com.numtory.application.ui.theme.REFRESH_TIMER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SekePriceViewModel
constructor(
    private val getSekePricesUseCase: GetSekePricesUseCase,

    ) : ViewModel() {


    private val _priceState = MutableStateFlow<ViewState<List<SekePrice>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<SekePrice>>> get() = _priceState.asStateFlow()

    init {
        getPrices()
    }

    fun getPrices() {
        _priceState.value = ViewState.Loading
        viewModelScope.launch {
            getSekePricesUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {
                        _priceState.value = ViewState.Success(response.result)
                    }

                    is ApiCallResult.Failure -> {
                        _priceState.value = ViewState.Failure(response.error)
                    }

                }

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}