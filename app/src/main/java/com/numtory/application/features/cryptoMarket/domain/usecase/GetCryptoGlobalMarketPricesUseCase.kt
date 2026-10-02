package com.numtory.application.features.cryptoMarket.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.EterexAssetsPrice
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.cryptoMarket.domain.entities.EterexAssetLabel
import com.numtory.application.features.cryptoMarket.domain.entities.LabelType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform

//class GetCryptoGlobalMarketPricesUseCase constructor(
//    private val marketRepository: CryptoGlobalMarketRepository,
//) {
//
//    fun action(): Flow<ApiCallResult<List<CryptoMarketPrice>>> {
//        return marketRepository.getCryptoPrices()
//    }
//}


class GetCryptoGlobalMarketPricesUseCase constructor(
    private val cryptoGlobalMarketRepository: CryptoGlobalMarketRepository,
) {

    fun action(): Flow<ApiCallResult<List<CryptoMarketPrice>>> {

        val flow1 = cryptoGlobalMarketRepository.getEterexAssetsPrice()
        val flow2 = cryptoGlobalMarketRepository.getCryptoPrices()

        return combine(flow1, flow2) { swap, market ->
            Pair(swap, market)
        }.transform { (assetPrices, assetInfo) ->

            val prices = mutableListOf<CryptoMarketPrice>()
            val eterexPrices = mutableListOf<EterexAssetsPrice>()

            when (assetPrices) {
                is ApiCallResult.Success -> {
                    eterexPrices.addAll((assetPrices.result ?: emptyList()). filter {  it.quoteAsset == "USDT" })
                    eterexPrices.sortByDescending  { it.volume }
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(assetPrices.error))
                }
            }

            when (assetInfo) {
                is ApiCallResult.Success -> {

                    eterexPrices.forEach { eterexP ->
                        val info: EterexAssetLabel? = assetInfo.result?.firstOrNull {
                            it.name?.equals(
                                eterexP.baseAsset,
                                ignoreCase = true
                            ) == true
                        }

                        prices.add(
                            CryptoMarketPrice(
                                persianName = info?.persianName,
                                price = eterexP.bestBuyPrice.toString(),
                                symbol = eterexP.baseAsset,
                                name = info?.fullName,
                                image = "https://static-dl.eterex.com/icons-v2/svg/${eterexP.baseAsset?.lowercase()}_.svg",
                                dayChangePercent = eterexP.priceChangePercentage?.toFloat() ?: 0f,
                                dayChangePrice = eterexP.priceChange?.toFloat() ?: 0f,
                                labels = info?.labels,
                            )
                        )

                    }
                    emit(ApiCallResult.Success(prices))
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(assetInfo.error))

                }
            }
        }

    }
}

