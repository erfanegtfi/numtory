package com.numtory.application.data.remote

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.data.utils.GeneralError
import com.numtory.application.data.utils.withErrorMessage
import com.numtory.application.data.utils.withErrorThrowable
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException


suspend fun <T> getResult(call: suspend () -> T): ApiCallResult<T> {
    return try {
        val result = call()
        onResponse(result)
    } catch (e: Exception) {
        ApiCallResult.Failure(mapError(e))
    }
}

fun mapError(e: Exception): GeneralError {
    return when (e) {

        is ClientRequestException -> {
            val response = e.response
            GeneralError().withErrorMessage(response.status.description)
        }

        is ServerResponseException -> {
            val response = e.response
            GeneralError().withErrorMessage(response.status.description)
            GeneralError().withErrorThrowable(e)
        }

//        is SocketTimeoutException -> {
//            GeneralError().withErrorThrowable(e)
//        }
//
//        is IOException -> {
//            GeneralError().withErrorThrowable(e)
//        }

        else -> {
            GeneralError().withErrorThrowable(e)
        }
    }
}

fun <T> onResponse(response: T): ApiCallResult<T> {
    return ApiCallResult.Success(response)
}