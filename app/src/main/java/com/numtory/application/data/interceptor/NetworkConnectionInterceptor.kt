package com.numtory.application.data.interceptor

import android.content.Context
import com.eterex.data.remote.utils.exception.NetworkConnectionException
import com.numtory.application.data.utils.isOnline
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor constructor(
    private val context: Context
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val network: Boolean = isOnline(context)
        if (!network) {
            throw NetworkConnectionException()
        }

        return chain.proceed(request)
    }
}