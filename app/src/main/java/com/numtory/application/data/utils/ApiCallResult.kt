package com.numtory.application.data.utils

sealed class ApiCallResult<out T> {
    data class Success<out T>(val result: T): ApiCallResult<T>()
    data class Failure(val error: GeneralError): ApiCallResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$result]"
            is Failure -> "Error[exception=]"
        }
    }
}