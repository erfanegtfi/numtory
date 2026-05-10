package com.numtory.application.features.market.data.dataSource

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
import com.numtory.application.features.market.data.models.TabdealDataModel
import com.numtory.application.features.market.data.models.TabdealMarketListDataModel
import com.numtory.application.features.market.data.models.TetherLandListDataModel
import com.numtory.application.features.market.data.models.TwoxDataModel
import com.numtory.application.features.market.data.models.WallexResultDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.numtory.application.features.market.data.models.Arz3PriceDataModel
import com.numtory.application.features.market.data.models.Arz3coinsDataModel
import com.numtory.application.features.market.data.models.ExchangeStatusDataModel
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

interface MarketRemoteDataSource {
    suspend fun getExchanges(): List<ExchangeStatusDataModel>
    suspend fun getBitPinPrice(marketId: Int): BitPinDataModel
    suspend fun getTetherLandPrice(): TetherLandListDataModel
    suspend fun getAbanTetherPrice(): AbanTetherListDataModel
    suspend fun getNobitexMarketPrice(): NobitexMarketListDataModel
    suspend fun getNobitexSwapPrice(market: String): NobitexSwapDataModel
    suspend fun getTabdealSwapPrice(fromCurrency: String, toCurrency: String): TabdealDataModel

    suspend fun getTabdealMarketPrice(): TabdealMarketListDataModel

    suspend fun getBit24SwapPrice(fromCurrency: String, toCurrency: String): Bit24SwapDataModel

    suspend fun getBit24MarketPrice(): Bit24MarketListDataModel

    suspend fun getArzplusSwapPrice(fromCurrency: String, toCurrency: String): ArzplusSwapDataModel

    suspend fun getArzplusMarketPrice(): ArzplusMarketListDataModel
    suspend fun getCoinkadePrice(): CoinkadeDataModel
    suspend fun getTwoxPrice(fromCurrency: String, toCurrency: String): TwoxDataModel
    suspend fun getPoolenoPrice(
        baseCurrency: String,
        quoteCurrency: String,
        isBuy: Boolean,
    ): PoolenoDataModel

    suspend fun getEterexPrice(): EterexPriceGroupsDataModel

    suspend fun getSarmayexMarketPrice(): SarmayexMarketListDataModel
    suspend fun getSarmayexSwapPrice(market: String): SarmayexSwapDataModel

    suspend fun getPingiSwapPrice(): PingiDataModel
    suspend fun getWallexSwapPrice(market: String): WallexResultDataModel
    suspend fun getSarafSwapPrice(): SarafPriceDataModel
    suspend fun getArz3SwapPrice(): Arz3coinsDataModel
    suspend fun getUbitexSwapPrice(baseCurrency: String, quoteCurrency: String): UbitexDataModel
}

class MarketRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : MarketRemoteDataSource {

    override suspend fun getExchanges(): List<ExchangeStatusDataModel> {
        val response = httpClient.get("http://10.0.2.2:8000/api/exchanges/")
        val json = response.bodyAsText()
        val type = object : TypeToken<List<ExchangeStatusDataModel>>() {}.type
        return gson.fromJson(json, type)
    }

    override suspend fun getBitPinPrice(marketId: Int): BitPinDataModel {
        val response = httpClient.get("https://api.bitpin.ir/v1/otc/price/?market_id=$marketId")
        val json = response.bodyAsText()
        return gson.fromJson(json, BitPinDataModel::class.java)
    }

    override suspend fun getTetherLandPrice(): TetherLandListDataModel {
//        val response = httpClient.get("https://service.tetherland.com/api/v4/currencies")
        val response = httpClient.get("https://service.tetherland.com/api/v5/currencies")
        val json = response.bodyAsText()
        return gson.fromJson(json, TetherLandListDataModel::class.java)
    }

    override suspend fun getAbanTetherPrice(): AbanTetherListDataModel {
        val response = httpClient.get("https://api.abantether.com/api/v2/manager/coins")
        val json = response.bodyAsText()
        return gson.fromJson(json, AbanTetherListDataModel::class.java)
    }

    override suspend fun getNobitexSwapPrice(market: String): NobitexSwapDataModel {
        val response = httpClient.get("https://apiv2.nobitex.ir/exchange/options") {
            parameter("market", market)
        }
        val json = response.bodyAsText()
        return gson.fromJson(json, NobitexSwapDataModel::class.java)
    }

    override suspend fun getNobitexMarketPrice(): NobitexMarketListDataModel {
        val response = httpClient.get("https://apiv2.nobitex.ir/market/stats")
        val json = response.bodyAsText()
        return gson.fromJson(json, NobitexMarketListDataModel::class.java)
    }

    override suspend fun getTabdealSwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): TabdealDataModel {
        val response =
            httpClient.get(
                "https://api-web.tabdeal.org/r/swap/prices_zero_commission_tier_based/"
            ) {
                parameter("from_currency", fromCurrency)
                parameter("to_currency", toCurrency)
            }
        val json = response.bodyAsText()
        return gson.fromJson(json, TabdealDataModel::class.java)
    }

    override suspend fun getTabdealMarketPrice(): TabdealMarketListDataModel {
        val response =
            httpClient.get(
                "https://api-web.tabdeal.org/r/plots/currencies/dynamic-info/"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, TabdealMarketListDataModel::class.java)
    }

    override suspend fun getBit24SwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): Bit24SwapDataModel {
        val response =
            httpClient.get(
                "https://otc-api.bit24.cash/api/v1/coins/convertor"
            ) {
                parameter("from_coin", fromCurrency)
                parameter("to_coin", toCurrency)
                parameter("amount", 1)
                parameter("type", "buy")
            }
        val json = response.bodyAsText()
        return gson.fromJson(json, Bit24SwapDataModel::class.java)
    }

    override suspend fun getBit24MarketPrice(): Bit24MarketListDataModel {
        val response =
            httpClient.get(
                "https://bit24.cash/api/v2/otc/v1/coins/list/all-in-one"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, Bit24MarketListDataModel::class.java)
    }

    override suspend fun getArzplusSwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): ArzplusSwapDataModel {
        val response =
            httpClient.get(
                "https://api.arzplus.net/api/v1/trade/otc/info/"
            ) {
                parameter("from", fromCurrency)
                parameter("to", toCurrency)
            }
        val json = response.bodyAsText()
        return gson.fromJson(json, ArzplusSwapDataModel::class.java)
    }

    override suspend fun getArzplusMarketPrice(): ArzplusMarketListDataModel {
        val response =
            httpClient.get(
                "https://api.arzplus.net/api/v1/asset/overview/"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, ArzplusMarketListDataModel::class.java)
    }

    override suspend fun getCoinkadePrice(): CoinkadeDataModel {
        val response =
            httpClient.get(
                "https://api.coinkade.biz/get-usdt-price"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, CoinkadeDataModel::class.java)
    }

    override suspend fun getTwoxPrice(
        fromCurrency: String,
        toCurrency: String
    ): TwoxDataModel {
        val response =
            httpClient.get(
                "https://api.prd.twox.info/api/currencies/prices/latest/$fromCurrency/$toCurrency"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, TwoxDataModel::class.java)
    }

    override suspend fun getPoolenoPrice(
        baseCurrency: String,
        quoteCurrency: String,
        isBuy: Boolean,
    ): PoolenoDataModel {
        val side = if (isBuy) "buy" else "sell"
        val response =
            httpClient.get(
                "https://api-beta.pooleno.ir/api/v1/trade/public/conversion-rate/$baseCurrency-$quoteCurrency/$side"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, PoolenoDataModel::class.java)
    }

    override suspend fun getEterexPrice(): EterexPriceGroupsDataModel {
        val response =
            httpClient.get(
                "https://api.eterex.com/api/Configs/v2"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, EterexPriceGroupsDataModel::class.java)
    }


    override suspend fun getSarmayexMarketPrice(): SarmayexMarketListDataModel {
        val response = httpClient.get("https://core.sarmayex.com/api/v1/pairs")
        val json = response.bodyAsText()
        return gson.fromJson(json, SarmayexMarketListDataModel::class.java)
    }

    override suspend fun getSarmayexSwapPrice(market: String): SarmayexSwapDataModel {
        val response = httpClient.get("https://api.sarmayex.com/api/v2/currency/symbol/$market")
        val json = response.bodyAsText()
        return gson.fromJson(json, SarmayexSwapDataModel::class.java)
    }

    override suspend fun getPingiSwapPrice(): PingiDataModel {
        val response = httpClient.get("https://api5.pingi.co/trading/market/prices/")
        val json = response.bodyAsText()
        return gson.fromJson(json, PingiDataModel::class.java)
    }

    override suspend fun getWallexSwapPrice(market: String): WallexResultDataModel {
        val response = httpClient.get("https://api.wallex.ir/v1/coin-market-list")
        {
            parameter("keys", market)
        }
        val json = response.bodyAsText()
        return gson.fromJson(json, WallexResultDataModel::class.java)
    }

    override suspend fun getSarafSwapPrice(): SarafPriceDataModel {
        val response = httpClient.get("https://api.sarafapp.com/v3/prices/crypto")

        val json = response.bodyAsText()
        return gson.fromJson(json, SarafPriceDataModel::class.java)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getArz3SwapPrice(): Arz3coinsDataModel {
        val timestamp = Clock.System.now().toEpochMilliseconds() / 1000
        val response = httpClient.post("https://app.arz3.com/api/v3/order/get-info") {
            header("X-Timestamp", timestamp.toString())
            header(
                "X-signature",
                "17682194f5c86a98341d7c5c429faf6f531b55deac96d7f7192eabf08cbc9198"
            )
        }

        val json = response.bodyAsText()
        return gson.fromJson(json, Arz3coinsDataModel::class.java)
    }

    override suspend fun getUbitexSwapPrice(
        baseCurrency: String,
        quoteCurrency: String
    ): UbitexDataModel {
        val response =
            httpClient.get("https://api.ubitex.io/api/pair/topprice/$baseCurrency$quoteCurrency")

        val json = response.bodyAsText()
        return gson.fromJson(json, UbitexDataModel::class.java)
    }

}