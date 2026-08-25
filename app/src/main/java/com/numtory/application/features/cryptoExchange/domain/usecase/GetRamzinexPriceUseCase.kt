package com.numtory.application.features.cryptoExchange.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoExchange.data.models.RamzinexCurrencyItemDataModel
import com.numtory.application.features.cryptoExchange.data.repositories.MarketRepository
import com.numtory.application.features.cryptoExchange.domain.entities.ExchangeInfo
import com.numtory.application.features.cryptoExchange.domain.entities.MarketPrice
import com.numtory.application.features.cryptoExchange.domain.enums.Exchanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform


class GetRamzinexPriceUseCase constructor(
    private val marketRepository: MarketRepository,
) {
    var assetBase: RamzinexCurrencyItemDataModel? = null
    var assetQuote: RamzinexCurrencyItemDataModel? = null
    fun action(
        base: String,
        quote: String,
    ): Flow<ApiCallResult<MarketPrice>> = flow {

        if (assetBase?.id != null && assetQuote != null && assetBase?.symbol?.lowercase() == base.lowercase())
            emitAll(getOtcPrice(base, assetBase!!.id.toString(), assetQuote!!.id.toString()))
        else
            marketRepository.getRamzinexCurrencies().collect { response ->
                when (response) {
                    is ApiCallResult.Success -> {

                        assetBase =
                            response.result?.data?.currencies?.firstOrNull { it.symbol?.lowercase() == base.lowercase() }
                        assetQuote =
                            response.result?.data?.currencies?.firstOrNull { it.symbol?.lowercase() == quote?.lowercase() }
                        if (assetBase?.id != null && assetQuote != null) {
                            emitAll(
                                getOtcPrice(
                                    base,
                                    assetBase!!.id.toString(),
                                    assetQuote!!.id.toString()
                                )
                            )
                        }

                    }

                    is ApiCallResult.Failure -> {
                        ApiCallResult.Failure(response.error)
                    }
                }
            }
    }


    fun getOtcPrice(

        base: String,
        fromId: String,
        toId: String,
    ): Flow<ApiCallResult<MarketPrice>> {
        val exchangesInfo = marketRepository.getSavedExchangesInfo()


        val flow1 = marketRepository.getRamzinexSwapPrice(toId, fromId, true)
        val flow2 = marketRepository.getRamzinexSwapPrice(fromId, toId, false)
//        val flow3 = marketRepository.getTabdealMarket()

        return combine(flow1, flow2) { buy, sell ->
            Pair(buy, sell)
        }.transform { (buyPriceResponse, sellPriceResponse) ->

            val swapPrice = MarketPrice(
                symbol = base,
                exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.ramzinex }
                    ?: ExchangeInfo(
                        exchange = Exchanges.ramzinex,
                        active = true,
                        display = true
                    ),
                lastRefresh = System.currentTimeMillis(),
            )

            when (buyPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.buyPrice = ((buyPriceResponse.result?.data?.fromAmount
                        ?: "0").toDouble() / 10).toString()
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(buyPriceResponse.error))
                }
            }

            when (sellPriceResponse) {
                is ApiCallResult.Success -> {
                    swapPrice.sellPrice = ((sellPriceResponse.result?.data?.toAmount
                        ?: "0").toDouble() / 10).toString()
                }

                is ApiCallResult.Failure -> {
                    emit(ApiCallResult.Failure(sellPriceResponse.error))
                }
            }
            emit(ApiCallResult.Success(swapPrice))
//            when (marketResponse) {
//                is ApiCallResult.Success -> {
//                    val marketPrice = MarketPrice(
//                        exchangeInfo = exchangesInfo?.firstOrNull { it.exchange == Exchanges.tabdealMarket }
//                            ?: ExchangeInfo(
//                                exchange = Exchanges.tabdealMarket,
//                                active = true,
//                                display = true
//                            ),
//                        marketPrice = marketResponse.result?.get(toCurrency)
//                            ?.get(fromCurrency)?.price,
//                        lastRefresh = System.currentTimeMillis()
//                    )
//
//                    emit(ApiCallResult.Success(marketPrice))
//                }
//
//                is ApiCallResult.Failure -> {
//                    emit(ApiCallResult.Failure(marketResponse.error))
//
//                }
//            }


        }

    }
}

