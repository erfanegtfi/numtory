package com.numtory.application.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient


class KtorHttpClient(
    private val okHttpClient: OkHttpClient

) {
    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                preconfigured = okHttpClient
                // added config in injected OkHttpClient
//                config {
//                    connectTimeout(30, TimeUnit.SECONDS)
//                    readTimeout(30, TimeUnit.SECONDS)
//                    writeTimeout(30, TimeUnit.SECONDS)
//                    retryOnConnectionFailure(true)
//                    addInterceptor(HttpLoggingInterceptor().apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    })
//                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }


            //to map json objects returned from the api to a kotlin data class
            install(ContentNegotiation) {
                json(Json {
                    //ignores json keys we have not included in our data class
                    ignoreUnknownKeys = true
                })
//                gson()
            }


            // a logger to see logging information about every request we make using the client
//            install(Logging) {
//                level = LogLevel.ALL
//            }
        }
    }

//    private val engine: OkHttpEngine,
//    fun create2(): HttpClient {
//      return  HttpClient(engine) {
//          //Makes the requests thorow an exception if the http status code is does not start with 2
//          expectSuccess = true
//
////            engine {
////                config {
////                    connectTimeout(30, TimeUnit.SECONDS)
////                    readTimeout(30, TimeUnit.SECONDS)
////                    writeTimeout(30, TimeUnit.SECONDS)
////                    retryOnConnectionFailure(true)
////                    addInterceptor(HttpLoggingInterceptor().apply {
////                        level = HttpLoggingInterceptor.Level.BODY
////                    })
////                }
////            }
//
//          // Install plugins
////            install(ContentNegotiation) {
////                gson()
////            }
//
//          defaultRequest {
//              contentType(ContentType.Application.Json)
//              accept(ContentType.Application.Json)
//          }
//
//
//          //to map json objects returned from the api to a kotlin data class
//          install(ContentNegotiation) {
//              json(Json {
//                  //ignores json keys we have not included in our data class
//                  ignoreUnknownKeys = true
//              })
//          }
//          //a logger to see logging information about every request we make using the client
////            install(Logging) {
////                level = LogLevel.ALL
////            }
//      }
//    }


}