package com.numtory.application.features.gold.data.dataSource

import com.numtory.application.features.market.data.models.BitPinDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.numtory.application.features.market.data.models.ExchangeInfoDataModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import com.numtory.application.BuildConfig
import com.numtory.application.features.gold.data.models.DigikalaDataModel
import com.numtory.application.features.gold.data.models.GoldExchangeInfoDataModel
import com.numtory.application.features.gold.data.models.GoldikaDataModel

interface GoldMarketRemoteDataSource {
    suspend fun getGoldExchanges(): List<GoldExchangeInfoDataModel>
    suspend fun getDigikalaPrice(): DigikalaDataModel
    suspend fun getGoldikaPrice(): GoldikaDataModel

}

class GoldMarketRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : GoldMarketRemoteDataSource {

    override suspend fun getGoldExchanges(): List<GoldExchangeInfoDataModel> {
        val response = httpClient.get(BuildConfig.NUMTORY_GOLD_EXCHANGES_URL)
        val json = response.bodyAsText()
        val type = object : TypeToken<List<GoldExchangeInfoDataModel>>() {}.type
        return gson.fromJson(json, type)
    }

    override suspend fun getDigikalaPrice(): DigikalaDataModel {
        val response = httpClient.get(BuildConfig.DIGIKALA_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, DigikalaDataModel::class.java)
    }

    override suspend fun getGoldikaPrice(): GoldikaDataModel {
        val response = httpClient.get(BuildConfig.GOLDIKA_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, GoldikaDataModel::class.java)
    }

}