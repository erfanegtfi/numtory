package com.numtory.application.features.menu;

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.common.getAppVersion
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.car.domain.entities.CarCategory
import com.numtory.application.features.car.domain.usecase.FilterCarPricesUseCase
import com.numtory.application.features.car.domain.usecase.GetCarPricesUseCase
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.main.SettingsUiState
import com.numtory.application.features.seke.domain.entities.SekePrice
import com.numtory.application.features.seke.domain.usecase.GetSekePricesUseCase
import com.numtory.application.features.setting.domain.entities.AppSettings
import com.numtory.application.features.setting.domain.usecase.GetSettingsUseCase
import com.ramcosta.composedestinations.generated.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.destinations.CarPriceScreenDestination
import com.ramcosta.composedestinations.generated.destinations.NetworkScanScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SekePriceScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class MenuViewModel
constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {

    private val _settingsState = MutableStateFlow<ViewState<List<MenuItem>>>(ViewState.Init)
    val settingsState: StateFlow<ViewState<List<MenuItem>>> get() = _settingsState.asStateFlow()

    init {
        getSettings()
    }

    fun getSettings() {
        viewModelScope.launch {
            getSettingsUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {

                        if (response.result?.car != true)
                            menuItems.removeAll { it.destination == CarPriceScreenDestination }
                        if (response.result?.fiat != true)
                            menuItems.removeAll { it.destination == SekePriceScreenDestination }

                        _settingsState.value = ViewState.Success(menuItems)

                    }

                    is ApiCallResult.Failure -> {
                        _settingsState.value = ViewState.Failure(response.error)
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

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val destination: DirectionDestinationSpec,
)

private var menuItems = mutableListOf(
    MenuItem(
        title = "نرخ ارز",
        icon = Icons.Default.CurrencyExchange,
        destination = SekePriceScreenDestination,
    ),
    MenuItem(
        title = "قیمت خودرو",
        icon = Icons.Default.DirectionsCar,
        destination = CarPriceScreenDestination,
    ),
    MenuItem(
        title = "اسکنر ارز دیجیتال",
        icon = Icons.Default.QrCodeScanner,
        destination = NetworkScanScreenDestination,
    ),
    MenuItem(
        title = "درباره اپلیکیشن",
        icon = Icons.Default.Info,
        destination = AboutScreenDestination,
    ),
)