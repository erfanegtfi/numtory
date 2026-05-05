package com.numtory.application.features.base

import com.numtory.application.data.utils.GeneralError


sealed class ViewState<out T> {
    data class Success<out T>(val data: T): ViewState<T>()
    data class Failure(val error: GeneralError): ViewState<Nothing>()
    object Loading: ViewState<Nothing>()
    object Init: ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Failure -> "Error[exception=]"
            is Loading -> "Loading"
            is Init -> "Init"
        }
    }
}