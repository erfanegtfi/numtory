package com.numtory.application.data.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class SimpleLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()

        return try {
            val response = chain.proceed(request)
            val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)

            val emoji = when (response.code) {
                in 200..299 -> "✅"
                in 300..399 -> "🔁"
                in 400..499 -> "⚠️"
                in 500..599 -> "❌"
                else -> "❓"
            }

            val message = buildString {
                append("$emoji ")
                append("${request.method} ")
                append("${request.url} ")
                append("Code: (${response.code}), ")
                append("Time: ${durationMs}ms")
            }

            Log.d(TAG, message)

            response
        } catch (e: Exception) {
            val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)

            val message = buildString {
                append("💥 ")
                append("${request.method} ")
                append("${request.url} ==>")
                append("FAILED ")
                append("${durationMs}ms ")
                append("(${e.javaClass.simpleName})")
            }

            Log.e(TAG, message)

            throw e
        }
    }

    companion object {
        private const val TAG = "SimpleLoggingInterceptor"
    }
}