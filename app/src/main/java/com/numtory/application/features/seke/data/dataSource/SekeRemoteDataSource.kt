package com.numtory.application.features.seke.data.dataSource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import com.numtory.application.BuildConfig
import com.numtory.application.features.cryptoMarket.data.models.CryptoMarketItemDataModel
import com.numtory.application.features.seke.data.models.SekeDataModel

interface SekeRemoteDataSource {
    suspend fun getSekePrices( ): List<SekeDataModel>

}

class SekeRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : SekeRemoteDataSource {

    override suspend fun getSekePrices(): List<SekeDataModel> {
        val response = httpClient.get(BuildConfig.SEKE_URL)
        val json = response.bodyAsText()
        val type = object : TypeToken<List<SekeDataModel>>() {}.type
        return gson.fromJson(json, type)
    }

}