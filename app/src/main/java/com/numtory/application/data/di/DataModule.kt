package com.numtory.application.data.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
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
import okio.Path.Companion.toOkioPath
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File
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

    single<ImageLoader> {

        ImageLoader.Builder(get())
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = { get<OkHttpClient>() }))
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(get(), 0.25)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(get<Context>().cacheDir.resolve("image_cache"))
                    .maxSizeBytes(10 * 1024 * 1024)  // 10MB
                    .build()
            }
            .build()
    }
}