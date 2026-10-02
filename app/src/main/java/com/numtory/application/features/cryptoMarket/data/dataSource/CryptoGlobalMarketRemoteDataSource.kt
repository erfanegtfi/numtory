package com.numtory.application.features.cryptoMarket.data.dataSource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import com.numtory.application.BuildConfig
import com.numtory.application.features.cryptoExchange.data.models.EterexAssetsPriceDataModel
import com.numtory.application.features.cryptoMarket.data.models.EterexAssetLabelResponse
import io.ktor.client.request.parameter

interface CryptoGlobalMarketRemoteDataSource {
    //    suspend fun getCryptoGlobalMarketPrices(page: Int ): List<CryptoMarketItemDataModel>
    suspend fun getCryptoGlobalMarketPrices(): EterexAssetLabelResponse
    suspend fun getEterexAssetsPrice(): List<EterexAssetsPriceDataModel>
}

class CryptoGlobalMarketRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : CryptoGlobalMarketRemoteDataSource {

//    override suspend fun getCryptoGlobalMarketPrices(page: Int ): List<CryptoMarketItemDataModel> {
//        val response = httpClient.get(BuildConfig.CRYPTO_GLOBAL_MARKET_URL){
//            parameter("vs_currency", "usd")
//            parameter("order", "market_cap_desc")
//            parameter("per_page", "250")
//            parameter("page", page)
//            parameter("sparkline", false)
//        }
//        val json = response.bodyAsText()
//        val type = object : TypeToken<List<CryptoMarketItemDataModel>>() {}.type
//        return gson.fromJson(json, type)
//    }


    override suspend fun getEterexAssetsPrice(): List<EterexAssetsPriceDataModel> {
        val response =
            httpClient.get(
                BuildConfig.ETEREX_PRICE_URL
            )
        val json = response.bodyAsText()
        val type = object : TypeToken<List<EterexAssetsPriceDataModel>>() {}.type
        return gson.fromJson(json, type)
    }


    override suspend fun getCryptoGlobalMarketPrices(): EterexAssetLabelResponse {
        val response = httpClient.get(BuildConfig.CRYPTO_GLOBAL_MARKET_URL)
        val json = response.bodyAsText()
        val type = object : TypeToken<EterexAssetLabelResponse>() {}.type
        return gson.fromJson(json, type)
    }
}