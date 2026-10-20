package com.numtory.application.features.car.data.dataSource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.numtory.application.BuildConfig
import com.numtory.application.features.car.data.model.CarCategoryDataModel
import com.numtory.application.features.car.data.model.CarCategoryResponse
import com.numtory.application.features.cryptoExchange.data.models.BitPinOTCDataModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

interface CarRemoteDataSource {
    suspend fun getCarPrices(): CarCategoryResponse
}

class CarRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : CarRemoteDataSource {


    override suspend fun getCarPrices(): CarCategoryResponse {
        val response = httpClient.get(BuildConfig.CAR_URL)
//        val a : CarCategoryResponse = response.body()
//        return a
        val type = object : TypeToken<CarCategoryResponse>() {}.type
        return gson.fromJson(response.bodyAsText(), type)
    }

}