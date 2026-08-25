package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.EterexGroups
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

class GetEterexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {

    fun action(assetSymbol: String): Flow<ApiCallResult<MarketPrice>> {

        val priceFlow = marketRepository.getEterexPrice()
        val assetsFlow = marketRepository.getEterexAssetsPrice()

        return priceFlow
            .zip(assetsFlow) { priceResponse, assetsResponse ->
                when (assetsResponse) {
                    is ApiCallResult.Success -> {

                        val asset =
                            assetsResponse.result?.firstOrNull { item -> item.symbol?.uppercase() == ((assetSymbol+"USDT").uppercase()) }

                        if ("usdt" == assetSymbol.lowercase())
                            getPrice(priceResponse, assetSymbol, "1")
                        else getPrice(priceResponse, assetSymbol, asset?.price)

                    }

                    is ApiCallResult.Failure -> {
                        ApiCallResult.Failure(assetsResponse.error)
                    }
                }
            }
    }


    fun getPrice(
        priceResponse: ApiCallResult<List<EterexGroups>?>,
        assetSymbol: String,
        price: String?
    ): ApiCallResult<MarketPrice> {
        return when (priceResponse) {
            is ApiCallResult.Success -> {
                val exchangesInfo = marketRepository.getSavedExchangesInfo()

                val asset =
                    priceResponse.result?.firstOrNull { item -> item.coins.contains(assetSymbol.uppercase()) }

                 ApiCallResult.Success(
                    MarketPrice(
                        symbol = assetSymbol,
                        buyPrice = ((asset?.price?.irtUsdt?.toDouble() ?: 0.0) * (price?.toDouble()
                            ?: 0.0)).toString(),
                        sellPrice = ((asset?.price?.usdtIrt?.toDouble() ?: 0.0) * (price?.toDouble()
                            ?: 0.0)).toString(),
                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.eterex }
                            ?: ExchangeInfo(
                                exchange = Exchanges.eterex,
                                active = true,
                                display = true
                            ),
                        lastRefresh = System.currentTimeMillis()
                    )
                )
            }

            is ApiCallResult.Failure -> {
                ApiCallResult.Failure(priceResponse.error)
            }
        }
    }
}