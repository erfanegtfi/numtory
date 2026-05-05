package com.numtory.application.data.interceptor

import android.util.Log
import com.numtory.application.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        if (!BuildConfig.DEBUG) {
            return chain.proceed(request)
        }

        val startTime = System.nanoTime()

        logRequest(request)

        val response = chain.proceed(request)

        val endTime = System.nanoTime()

        logResponse(response, endTime - startTime)

        return response
    }

    // ==============================
    // Logging
    // ==============================

    private fun logRequest(request: Request) {
        val requestBody = request.body?.let { bodyToString(request) }

        val message = buildString {
            appendLine("➡️ REQUEST")
            append("URL: ${request.url} ")
            appendLine("Method: ${request.method} ")
//            appendLine("Headers: ${request.headers}")

            if (!requestBody.isNullOrBlank()) {
                appendLine("Body:")
                appendLine(requestBody)
            }

            appendLine("--------------------------------------------------")
        }

        Log.d(TAG, message)
    }

    private fun logResponse(response: Response, durationNs: Long) {
        val durationMs = TimeUnit.NANOSECONDS.toMillis(durationNs)

        val responseBodyString = try {
            response.peekBody(Long.MAX_VALUE).string()
        } catch (e: Exception) {
            "Unable to read response body"
        }

        val prettyBody = prettyJson(responseBodyString)

        val message = buildString {
            appendLine("⬅️ RESPONSE ")
            append("URL: ${response.request.url} ")
            append("Status: ${response.code}, ")
            appendLine("Time: ${durationMs}ms")

            if (prettyBody.isNotBlank()) {
                appendLine("Body:")
                appendLine(prettyBody)
            }

            appendLine("--------------------------------------------------")
        }

        logLargeMessage(message)
    }

    // ==============================
    // Helpers
    // ==============================

    private fun bodyToString(request: Request): String {
        return try {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            prettyJson(buffer.readString(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            "Unable to read request body"
        }
    }

    private fun prettyJson(raw: String): String {
        return try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonElement = JsonParser.parseString(raw)
            gson.toJson(jsonElement)
        } catch (e: JsonSyntaxException) {
            raw // fallback if not JSON
        }
    }

    private fun logLargeMessage(message: String) {
        val chunkSize = 4000
        var i = 0
        while (i < message.length) {
            Log.d(TAG, message.substring(i, minOf(message.length, i + chunkSize)))
            i += chunkSize
        }
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }
}