package com.numtory.application.data.di

import com.numtory.application.data.interceptor.LoggingInterceptor
import com.numtory.application.data.interceptor.SimpleLoggingInterceptor
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.data.remote.KtorHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val dataModule = module {

    single<Gson> {
//        Gson()
        GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()
    }

    single<LoggingInterceptor> {
        LoggingInterceptor()
    }

    single<SimpleLoggingInterceptor> {
        SimpleLoggingInterceptor()
    }

    single<OkHttpClient> {

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
//           .addInterceptor(get<LoggingInterceptor>())
            .addInterceptor(get<SimpleLoggingInterceptor>())
            .followRedirects(true)
            .build()


    }

    single<HttpClient> {
        KtorHttpClient(get<OkHttpClient>()).create()
    }

    single<Session> {
        Session(get())
    }


    single<CoroutineDispatcher>(qualifier = named("IO")) { Dispatchers.IO }
    single<CoroutineDispatcher>(qualifier = named("Default")) { Dispatchers.Default }
    single<CoroutineDispatcher>(qualifier = named("Main")) { Dispatchers.Main }
}