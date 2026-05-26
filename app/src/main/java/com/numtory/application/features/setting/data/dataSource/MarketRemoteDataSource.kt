package com.numtory.application.features.setting.data.dataSource

import com.numtory.application.features.market.data.models.AbanTetherListDataModel
import com.numtory.application.features.market.data.models.ArzplusMarketListDataModel
import com.numtory.application.features.market.data.models.ArzplusSwapDataModel
import com.numtory.application.features.market.data.models.Bit24MarketListDataModel
import com.numtory.application.features.market.data.models.Bit24SwapDataModel
import com.numtory.application.features.market.data.models.BitPinDataModel
import com.numtory.application.features.market.data.models.CoinkadeDataModel
import com.numtory.application.features.market.data.models.EterexPriceGroupsDataModel
import com.numtory.application.features.market.data.models.NobitexMarketListDataModel
import com.numtory.application.features.market.data.models.NobitexSwapDataModel
import com.numtory.application.features.market.data.models.PingiDataModel
import com.numtory.application.features.market.data.models.PoolenoDataModel
import com.numtory.application.features.market.data.models.SarmayexMarketListDataModel
import com.numtory.application.features.market.data.models.SarmayexSwapDataModel
import com.numtory.application.features.market.data.models.TabdealMarketListDataModel
import com.numtory.application.features.market.data.models.TetherLandListDataModel
import com.numtory.application.features.market.data.models.TwoxDataModel
import com.numtory.application.features.market.data.models.WallexResultDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.numtory.application.features.market.data.models.Arz3coinsDataModel
import com.numtory.application.features.market.data.models.ExchangeInfoDataModel
import com.numtory.application.features.market.data.models.SarafPriceDataModel
import com.numtory.application.features.market.data.models.UbitexDataModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import com.numtory.application.BuildConfig
import com.numtory.application.features.market.data.models.ArzinjaDataModel
import com.numtory.application.features.market.data.models.ArzyptoDataModel
import com.numtory.application.features.market.data.models.RamzinexDataModel
import com.numtory.application.features.market.data.models.TabdealSwapDataModel
import com.numtory.application.features.market.domain.entities.Arzypto
import com.numtory.application.features.setting.data.models.AppSettingsDataModel
import io.ktor.client.request.setBody

interface MarketRemoteDataSource {
    suspend fun getAppSettings(): AppSettingsDataModel

}

class MarketRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : MarketRemoteDataSource {

    override suspend fun getAppSettings(): AppSettingsDataModel {
        val response =
            httpClient.get(BuildConfig.APP_SETTINGS_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, AppSettingsDataModel::class.java)
    }
}