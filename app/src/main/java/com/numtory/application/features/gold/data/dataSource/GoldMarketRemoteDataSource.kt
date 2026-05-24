package com.numtory.application.features.gold.data.dataSource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import com.numtory.application.BuildConfig
import com.numtory.application.features.gold.data.models.DigikalaDataModel
import com.numtory.application.features.gold.data.models.GoldExchangeInfoDataModel
import com.numtory.application.features.gold.data.models.GoldikaDataModel
import com.numtory.application.features.gold.data.models.HamrahGoldDataModel
import com.numtory.application.features.gold.data.models.MelliGoldDataModel
import com.numtory.application.features.gold.data.models.TalaSeaDataModel
import com.numtory.application.features.gold.data.models.TlynDataModel
import io.ktor.client.request.parameter
import io.ktor.http.parameters

interface GoldMarketRemoteDataSource {
    suspend fun getGoldExchanges(): List<GoldExchangeInfoDataModel>
    suspend fun getDigikalaPrice(): DigikalaDataModel
    suspend fun getGoldikaPrice(): GoldikaDataModel
    suspend fun getHamrahGoldPrice(): HamrahGoldDataModel
    suspend fun getTlynPrice(): TlynDataModel
    suspend fun getMelliGoldPrice(): MelliGoldDataModel
    suspend fun getTalaseaPrice(): TalaSeaDataModel

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

    override suspend fun getHamrahGoldPrice(): HamrahGoldDataModel {
        val response = httpClient.get(BuildConfig.HAMRAH_GOLD_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, HamrahGoldDataModel::class.java)
    }

    override suspend fun getTlynPrice(): TlynDataModel {
        val response = httpClient.get(BuildConfig.TLYN_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, TlynDataModel::class.java)
    }

    override suspend fun getMelliGoldPrice(): MelliGoldDataModel {
        val response = httpClient.get(BuildConfig.MELLIGOLD_URL){
            parameter("symbol", "XAU18")
            parameter("format", "json")
        }
        val json = response.bodyAsText()
        return gson.fromJson(json, MelliGoldDataModel::class.java)
    }

    override suspend fun getTalaseaPrice(): TalaSeaDataModel {
        val response = httpClient.get(BuildConfig.TALASEA_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, TalaSeaDataModel::class.java)
    }
}