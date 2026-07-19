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
import com.numtory.application.features.market.data.models.BitPinOTCDataModel
import com.numtory.application.features.market.data.models.BitbargDataModel
import com.numtory.application.features.market.data.models.EterexAssetsPriceDataModel
import com.numtory.application.features.market.data.models.ExonyxDataModel
import com.numtory.application.features.market.data.models.AsacoineDataModel
import com.numtory.application.features.market.data.models.MorbitDataModel
import com.numtory.application.features.market.data.models.RamzinexCurrenciesDataModel
import com.numtory.application.features.market.data.models.RamzinexDataModel
import com.numtory.application.features.market.data.models.TabdealSwapDataModel
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders

interface MarketRemoteDataSource {
    suspend fun getBitPinPrice(marketId: Int): BitPinOTCDataModel
    suspend fun getBitPinMarketPrice(): List<BitPinDataModel>
    suspend fun getTetherLandPrice(): TetherLandListDataModel
    suspend fun getAbanTetherPrice(): AbanTetherListDataModel
    suspend fun getNobitexMarketPrice(): NobitexMarketListDataModel
    suspend fun getNobitexSwapPrice(market: String): NobitexSwapDataModel
    suspend fun getTabdealSwapPrice(fromCurrency: String, toCurrency: String): TabdealSwapDataModel

    suspend fun getTabdealMarketPrice(): TabdealMarketListDataModel

    suspend fun getExchanges(): List<ExchangeInfoDataModel>

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
    suspend fun getEterexAssetsPrice(): List<EterexAssetsPriceDataModel>

    suspend fun getSarmayexMarketPrice(): SarmayexMarketListDataModel
    suspend fun getSarmayexSwapPrice(market: String): SarmayexSwapDataModel

    suspend fun getPingiSwapPrice(): PingiDataModel
    suspend fun getWallexSwapPrice(market: String): WallexResultDataModel
    suspend fun getSarafSwapPrice(): SarafPriceDataModel
    suspend fun getArz3SwapPrice(): Arz3coinsDataModel
    suspend fun getUbitexSwapPrice(baseCurrency: String, quoteCurrency: String): UbitexDataModel
    suspend fun getRamzinexSwapPrice(
        fromId: String,
        toId: String,
        toAmount: Int? = null,
        fromAmount: Int? = null,
    ): RamzinexDataModel

    suspend fun getRamzinexCurrencies(): RamzinexCurrenciesDataModel

    suspend fun getArzinjaPrice(baseCurrency: String, providerType: String): ArzinjaDataModel

    suspend fun getArzyptoSwapPrice(
        currency: String,
        side: String,
        symbol: String,
    ): ArzyptoDataModel

    suspend fun getExonyxSwapPrice(): ExonyxDataModel

    suspend fun getBitbargPrice(
        base: String,
        symbol: String,
    ): BitbargDataModel

    suspend fun getMorbitPrice(symbol: String): MorbitDataModel

    suspend fun getAsacoinePrice(): AsacoineDataModel
}

class MarketRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : MarketRemoteDataSource {


    override suspend fun getBitPinPrice(marketId: Int): BitPinOTCDataModel {
        val response = httpClient.get("${BuildConfig.BITPIN_URL}$marketId")
        val json = response.bodyAsText()
        return gson.fromJson(json, BitPinOTCDataModel::class.java)
    }

    override suspend fun getBitPinMarketPrice(): List<BitPinDataModel> {
        val response = httpClient.get(BuildConfig.BITPIN_MARKET_URL)
        val json = response.bodyAsText()
        val type = object : TypeToken<List<BitPinDataModel>>() {}.type
        return gson.fromJson(json, type)
    }

    override suspend fun getExchanges(): List<ExchangeInfoDataModel> {
        val response = httpClient.get(BuildConfig.NUMTORY_EXCHANGES_URL)
        val json = response.bodyAsText()
        val type = object : TypeToken<List<ExchangeInfoDataModel>>() {}.type
        return gson.fromJson(json, type)
    }

    override suspend fun getTetherLandPrice(): TetherLandListDataModel {
//        val response = httpClient.get("https://service.tetherland.com/api/v4/currencies")
        val response = httpClient.get(BuildConfig.TETHERLAND_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, TetherLandListDataModel::class.java)
    }

    override suspend fun getAbanTetherPrice(): AbanTetherListDataModel {
        val response = httpClient.get(BuildConfig.ABANTEHTER_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, AbanTetherListDataModel::class.java)
    }

    override suspend fun getNobitexSwapPrice(market: String): NobitexSwapDataModel {
        val response = httpClient.get(BuildConfig.NUBITEX_URL) {
            parameter("market", market)
        }
        val json = response.bodyAsText()
        return gson.fromJson(json, NobitexSwapDataModel::class.java)
    }

    override suspend fun getNobitexMarketPrice(): NobitexMarketListDataModel {
        val response = httpClient.get(BuildConfig.NUBITEX_MARKET_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, NobitexMarketListDataModel::class.java)
    }

    override suspend fun getTabdealSwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): TabdealSwapDataModel {
        val response =
            httpClient.get(
                BuildConfig.TABDEAL_URL
            ) {
                parameter("from_currency", fromCurrency)
                parameter("to_currency", toCurrency)
            }
        val json = response.bodyAsText()
        return gson.fromJson(json, TabdealSwapDataModel::class.java)
    }

    override suspend fun getTabdealMarketPrice(): TabdealMarketListDataModel {
        val response =
            httpClient.get(
                BuildConfig.TABDEAL_MARKET_URL
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
                BuildConfig.BIT24_URL
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
                BuildConfig.BIT24_MARKET_URL
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
                BuildConfig.ARZPLUS_URL
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
                BuildConfig.ARZPLUS_MARKET_URL
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, ArzplusMarketListDataModel::class.java)
    }

    override suspend fun getCoinkadePrice(): CoinkadeDataModel {
        val response =
            httpClient.get(
                BuildConfig.COINKADE_URL
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
                "${BuildConfig.TWOX_URL}$fromCurrency/$toCurrency"
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
                "${BuildConfig.POOLENO_URL}$baseCurrency-$quoteCurrency/$side"
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, PoolenoDataModel::class.java)
    }

    override suspend fun getEterexPrice(): EterexPriceGroupsDataModel {
        val response =
            httpClient.get(
                BuildConfig.ETEREX_URL
            )
        val json = response.bodyAsText()
        return gson.fromJson(json, EterexPriceGroupsDataModel::class.java)
    }

    override suspend fun getEterexAssetsPrice(): List<EterexAssetsPriceDataModel> {
        val response =
            httpClient.get(
                BuildConfig.ETEREX_PRICE_URL
            )
        val json = response.bodyAsText()
        val type = object : TypeToken<List<EterexAssetsPriceDataModel>>() {}.type
        return gson.fromJson(json, type)
    }


    override suspend fun getSarmayexMarketPrice(): SarmayexMarketListDataModel {
        val response = httpClient.get(BuildConfig.SARMAYEX_MARKET_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, SarmayexMarketListDataModel::class.java)
    }

    override suspend fun getSarmayexSwapPrice(market: String): SarmayexSwapDataModel {
        val response = httpClient.get("${BuildConfig.SARMAYEX_URL}$market")
        val json = response.bodyAsText()
        return gson.fromJson(json, SarmayexSwapDataModel::class.java)
    }

    override suspend fun getPingiSwapPrice(): PingiDataModel {
        val response = httpClient.get(BuildConfig.PINGI_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, PingiDataModel::class.java)
    }

    override suspend fun getWallexSwapPrice(market: String): WallexResultDataModel {
        val response = httpClient.get(BuildConfig.WALLEX_URL)
        {
            parameter("keys", market)
        }
        val json = response.bodyAsText()
        return gson.fromJson(json, WallexResultDataModel::class.java)
    }

    override suspend fun getSarafSwapPrice(): SarafPriceDataModel {
        val response = httpClient.get(BuildConfig.SARAF_URL)

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
                ""
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
            httpClient.get("${BuildConfig.UBITEX_URL}$baseCurrency$quoteCurrency")

        val json = response.bodyAsText()
        return gson.fromJson(json, UbitexDataModel::class.java)
    }

    override suspend fun getRamzinexSwapPrice(
        fromId: String,
        toId: String,
        toAmount: Int?,
        fromAmount: Int?,
    ): RamzinexDataModel {
        val response =
            httpClient.get("${BuildConfig.RAMZINEX_URL}$fromId/$toId") {
                if (toAmount != null)
                    parameter("to_amount", toAmount)
                if (fromAmount != null)
                    parameter("from_amount", fromAmount)
            }

        val json = response.bodyAsText()
        return gson.fromJson(json, RamzinexDataModel::class.java)
    }

    override suspend fun getRamzinexCurrencies(): RamzinexCurrenciesDataModel {
        val response = httpClient.get(BuildConfig.RAMZINEX_CURRENCIES_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, RamzinexCurrenciesDataModel::class.java)
    }

    override suspend fun getArzinjaPrice(
        baseCurrency: String,
        providerType: String,
    ): ArzinjaDataModel {
        val response =
            httpClient.get(BuildConfig.ARZINJA_URL) {
                parameter("page", 1)
                parameter("per_page", 1)
                parameter("base_asset", baseCurrency)
                parameter("provider_type", providerType)
            }

        val json = response.bodyAsText()
        return gson.fromJson(json, ArzinjaDataModel::class.java)
    }

    override suspend fun getArzyptoSwapPrice(
        currency: String,
        side: String,
        symbol: String
    ): ArzyptoDataModel {
        val response =
            httpClient.post(BuildConfig.ARZYPTO_URL) {
                setBody(
                    mapOf(
                        "currency" to currency,
                        "side" to side,
                        "symbol" to symbol
                    )
                )

            }

        val json = response.bodyAsText()
        return gson.fromJson(json, ArzyptoDataModel::class.java)
    }

    override suspend fun getExonyxSwapPrice(): ExonyxDataModel {
        val response =
            httpClient.get(BuildConfig.EXONYX_URL)

        val json = response.bodyAsText()
        return gson.fromJson(json, ExonyxDataModel::class.java)
    }

    override suspend fun getBitbargPrice(
        base: String,
        symbol: String
    ): BitbargDataModel {
        val response =
            httpClient.get(BuildConfig.BITBARG_URL) {
                parameter("base", base)
                parameter("symbol", symbol)
            }

        val json = response.bodyAsText()
        return gson.fromJson(json, BitbargDataModel::class.java)
    }

    override suspend fun getMorbitPrice(symbol: String): MorbitDataModel {
        val response =
            httpClient.get("${BuildConfig.MORBIT_URL}${symbol.lowercase()}/otcprices")

        val json = response.bodyAsText()
        return gson.fromJson(json, MorbitDataModel::class.java)
    }

    override suspend fun getAsacoinePrice(): AsacoineDataModel {
        val response = httpClient.get(BuildConfig.ASACOINE_URL){
//            header("Accept-Charset", "UTF-8")
//            header("Content-Type", "application/json; charset=UTF-8")
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.ContentType, "application/json")
            header(HttpHeaders.AcceptCharset, "UTF-8")
        }

        val json = response.bodyAsText()
        return gson.fromJson(json, AsacoineDataModel::class.java)
    }

}