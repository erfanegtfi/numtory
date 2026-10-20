package com.numtory.application.features.car.presenter;

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.car.domain.usecase.FilterCarPricesUseCase
import com.numtory.application.features.car.domain.usecase.GetCarPricesUseCase
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.domain.entities.SekePrice
import com.numtory.application.features.seke.domain.usecase.GetSekePricesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class CarPriceViewModel
constructor(
    private val getCarPricesUseCase: GetCarPricesUseCase,
    private val filterCarPricesUseCase: FilterCarPricesUseCase,
) : ViewModel() {

    var carPrices: MutableList<CarCategory> = mutableListOf()
    private val _priceState = MutableStateFlow<ViewState<List<CarCategory>>>(ViewState.Init)
    val priceState: StateFlow<ViewState<List<CarCategory>>> get() = _priceState.asStateFlow()
    var searchQuery: String? = null

    init {
        getPrices()
    }

    fun getPrices() {
        _priceState.value = ViewState.Loading
        viewModelScope.launch {
            getCarPricesUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {
                        carPrices.clear()
                        if (response.result != null)
                            carPrices.addAll(response.result)
                        emitData()
                    }

                    is ApiCallResult.Failure -> {
                        _priceState.value = ViewState.Failure(response.error)
                    }

                }

            }
        }
    }

    fun filter(searchQuery: String? = null) {
        this.searchQuery = searchQuery
        emitData()
    }

    fun emitData() {
        _priceState.value =
            ViewState.Success(filterCarPricesUseCase(carPrices, searchQuery))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}