package com.numtory.application.features.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numtory.application.common.getAppVersion
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.base.ViewState
import com.numtory.application.features.setting.domain.entities.AppSettings
import com.numtory.application.features.setting.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@SuppressLint("CheckResult")
class SettingsViewModel
constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val context: Context,
) : ViewModel() {


    private val _settingsState = MutableStateFlow<ViewState<AppSettings?>>(ViewState.Init)
    val settingsState: StateFlow<ViewState<AppSettings?>> get() = _settingsState.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()


    init {
        getSettings()
    }

    fun getSettings() {
        viewModelScope.launch {
            getSettingsUseCase.action().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {
                        _settingsState.value = ViewState.Success(response.result)
                        if (getAppVersion(context) < (response.result?.version ?: 0) && response.result?.force == true)
                            _showSuccessDialog.value = true
                        else _showSuccessDialog.value = false
                    }

                    is ApiCallResult.Failure -> {
                        _settingsState.value = ViewState.Failure(response.error)
                    }

                }

            }
        }
    }

    fun closeDialog() {
        _showSuccessDialog.value = false
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}