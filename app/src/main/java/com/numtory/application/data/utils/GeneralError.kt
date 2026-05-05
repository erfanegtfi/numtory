package com.numtory.application.data.utils

import com.numtory.application.data.remote.baseResponse.ApiBaseResponse
import com.google.gson.Gson
import kotlin.toString
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class GeneralError {
    var errorType: ErrorType? = null
    var errorBody: ApiBaseResponse? = null
    var throwable: Throwable? = null
    var message: String? = null

}


fun GeneralError.withErrorMessage(message: String? = null): GeneralError {
    this.message = message
    return this
}

fun GeneralError.withErrorResponse(errorBody: ApiBaseResponse? = null): GeneralError {
    this.errorBody = errorBody
    if(errorBody?.message is String)
        this.message = errorBody.message.toString()
    return this
}


fun GeneralError.withErrorThrowable(throwable: Throwable?): GeneralError {

    this.throwable = throwable

    when (throwable) {

        is ClientRequestException -> { // 4xx
            val status = throwable.response.status.value

            this.errorType = when (status) {
                401, 403 -> ErrorType.AUTHORIZED
                404 -> ErrorType.NOT_FOUND
                400 -> ErrorType.BAD_REQUEST
                else -> ErrorType.UNKNOWN_REMOTE
            }

            val body = runBlocking { throwable.response.bodyAsText() }
            this.message =  body
//            this.errorBody = UtilsError2.parseError(body)
//            this.message = errorBody?.message ?: throwable.message
        }

        is ServerResponseException -> { // 5xx
            this.errorType = ErrorType.SERVER_ERROR

            val body = runBlocking { throwable.response.bodyAsText() }
//            this.errorBody = UtilsError2.parseError(body)
//            this.message = errorBody?.message ?: throwable.message
            this.message =  body
        }

        is ResponseException -> { // fallback
            val body = runBlocking { throwable.response.bodyAsText() }
            this.message =  body
//            this.errorBody = UtilsError2.parseError(body)
//            this.message = errorBody?.message ?: throwable.message
        }

        else -> {
            this.errorType = ErrorType.UNKNOWN
            this.message = throwable?.message
        }
    }

    return this
}

object UtilsError2 {
    private val gson = Gson()

    fun parseError(json: String?): ApiBaseResponse? {
        return try {
            gson.fromJson(json, ApiBaseResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}