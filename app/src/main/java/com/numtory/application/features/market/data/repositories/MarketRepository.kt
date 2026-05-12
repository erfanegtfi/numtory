package com.numtory.application.features.market.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.domain.entities.AbanTether
import com.numtory.application.features.market.domain.entities.Arz3CoinItem
import com.numtory.application.features.market.domain.entities.ExchangeInfo
import com.numtory.application.features.market.domain.entities.ArzplusMarketItem
import com.numtory.application.features.market.domain.entities.ArzplusSwap
import com.numtory.application.features.market.domain.entities.Bit24
import com.numtory.application.features.market.domain.entities.Bit24Swap
import com.numtory.application.features.market.domain.entities.BitPin
import com.numtory.application.features.market.domain.entities.Coinkade
import com.numtory.application.features.market.domain.entities.EterexGroups
import com.numtory.application.features.market.domain.entities.Nobitex
import com.numtory.application.features.market.domain.entities.NobitexMarket
import com.numtory.application.features.market.domain.entities.PingiItem
import com.numtory.application.features.market.domain.entities.Pooleno
import com.numtory.application.features.market.domain.entities.SarafPrice
import com.numtory.application.features.market.domain.entities.SarmayexMarketItem
import com.numtory.application.features.market.domain.entities.SarmayexSwap
import com.numtory.application.features.market.domain.entities.Tabdeal
import com.numtory.application.features.market.domain.entities.TabdealMarketItem
import com.numtory.application.features.market.domain.entities.TetherLand
import com.numtory.application.features.market.domain.entities.Twox
import com.numtory.application.features.market.domain.entities.Ubitex
import com.numtory.application.features.market.domain.entities.WallexMarkets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface MarketRepository {
    fun getExchanges(): Flow<ApiCallResult<List<ExchangeInfo>>>
    fun getSavedExchangesInfo(): List<ExchangeInfo>?
    fun getBitPin(marketId: Int): Flow<ApiCallResult<BitPin>>
    fun getTetherLand(): Flow<ApiCallResult<List<TetherLand>>>
    fun getAbanTether(): Flow<ApiCallResult<List<AbanTether>>>

    fun getNobitex(market: String): Flow<ApiCallResult<Nobitex?>>
    fun getNobitexMarket(): Flow<ApiCallResult<Map<String, NobitexMarket>>>

    fun getTabdeal(fromCurrency: String, toCurrency: String): Flow<ApiCallResult<Tabdeal>>
    fun getTabdealMarket(): Flow<ApiCallResult<Map<String, Map<String, TabdealMarketItem>>?>>

    fun getBit24SwapPrice(fromCurrency: String, toCurrency: String): Flow<ApiCallResult<Bit24Swap>>
    fun getBit24MarketPrice(): Flow<ApiCallResult<List<Bit24>>>

    fun getArzplusSwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): Flow<ApiCallResult<ArzplusSwap>>

    fun getArzplusMarketPrice(): Flow<ApiCallResult<List<ArzplusMarketItem>>>

    fun getTwoxPrice(fromCurrency: String, toCurrency: String): Flow<ApiCallResult<Twox>>
    fun getCoinkadePrice(): Flow<ApiCallResult<Coinkade>>
    fun getPoolenoPrice(
        baseCurrency: String,
        quoteCurrency: String,
        isBuy: Boolean
    ): Flow<ApiCallResult<Pooleno>>

    fun getEterexPrice(): Flow<ApiCallResult<List<EterexGroups>>>

    fun getSarmayex(market: String): Flow<ApiCallResult<SarmayexSwap?>>
    fun getSarmayexMarket(): Flow<ApiCallResult<Map<String, SarmayexMarketItem>?>>
    fun getPingi(): Flow<ApiCallResult<Map<String, PingiItem>>>
    fun getWallex(market: String): Flow<ApiCallResult<WallexMarkets>>
    fun getSaraf(): Flow<ApiCallResult<SarafPrice>>
    fun getArz3Price(): Flow<ApiCallResult<List<Arz3CoinItem>?>>
    fun getUbitexPrice(baseCurrency: String, quoteCurrency: String): Flow<ApiCallResult<Ubitex?>>
}

class MarketRepositoryImpl(
    private val marketRemoteDataSource: MarketRemoteDataSource,
    private val exchangesLocalDataSource: ExchangesLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : MarketRepository {

    override fun getExchanges(): Flow<ApiCallResult<List<ExchangeInfo>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getExchanges()
        }
        if (response is ApiCallResult.Success) {
            exchangesLocalDataSource.saveExchangesInfo(response.result)
            emit(ApiCallResult.Success(response.result.map { it.toEntity() }))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getSavedExchangesInfo(): List<ExchangeInfo>? {
        return exchangesLocalDataSource.getExchangesInfo()
    }

    override fun getBitPin(marketId: Int): Flow<ApiCallResult<BitPin>> = flow {
        val response = getResult {
            marketRemoteDataSource.getBitPinPrice(marketId)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getTetherLand(): Flow<ApiCallResult<List<TetherLand>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTetherLandPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getAbanTether(): Flow<ApiCallResult<List<AbanTether>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getAbanTetherPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getNobitexMarket(): Flow<ApiCallResult<Map<String, NobitexMarket>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getNobitexMarketPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getNobitex(market: String): Flow<ApiCallResult<Nobitex?>> = flow {
        val response = getResult {
            marketRemoteDataSource.getNobitexSwapPrice(market)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.result?.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


    override fun getTabdealMarket(): Flow<ApiCallResult<Map<String, Map<String, TabdealMarketItem>>?>> =
        flow {
            val response = getResult {
                marketRemoteDataSource.getTabdealMarketPrice()
            }
            if (response is ApiCallResult.Success) {
                emit(ApiCallResult.Success(response.result.toEntity()))
            } else if (response is ApiCallResult.Failure)
                emit(ApiCallResult.Failure(response.error))
        }.flowOn(dispatcher)

    override fun getTabdeal(
        fromCurrency: String,
        toCurrency: String
    ): Flow<ApiCallResult<Tabdeal>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTabdealSwapPrice(fromCurrency, toCurrency)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getBit24MarketPrice(): Flow<ApiCallResult<List<Bit24>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getBit24MarketPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getBit24SwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): Flow<ApiCallResult<Bit24Swap>> = flow {
        val response = getResult {
            marketRemoteDataSource.getBit24SwapPrice(fromCurrency, toCurrency)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getArzplusMarketPrice(): Flow<ApiCallResult<List<ArzplusMarketItem>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getArzplusMarketPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


    override fun getArzplusSwapPrice(
        fromCurrency: String,
        toCurrency: String
    ): Flow<ApiCallResult<ArzplusSwap>> = flow {
        val response = getResult {
            marketRemoteDataSource.getArzplusSwapPrice(fromCurrency, toCurrency)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getTwoxPrice(
        fromCurrency: String,
        toCurrency: String
    ): Flow<ApiCallResult<Twox>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTwoxPrice(fromCurrency, toCurrency)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getCoinkadePrice(): Flow<ApiCallResult<Coinkade>> = flow {
        val response = getResult {
            marketRemoteDataSource.getCoinkadePrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getPoolenoPrice(
        baseCurrency: String, quoteCurrency: String, isBuy: Boolean
    ): Flow<ApiCallResult<Pooleno>> = flow {
        val response = getResult {
            marketRemoteDataSource.getPoolenoPrice(baseCurrency, quoteCurrency, isBuy)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getEterexPrice(): Flow<ApiCallResult<List<EterexGroups>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getEterexPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }


    override fun getSarmayex(market: String): Flow<ApiCallResult<SarmayexSwap?>> = flow {
        val response = getResult {
            marketRemoteDataSource.getSarmayexSwapPrice(market)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getSarmayexMarket(): Flow<ApiCallResult<Map<String, SarmayexMarketItem>?>> = flow {
        val response = getResult {
            marketRemoteDataSource.getSarmayexMarketPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getPingi(): Flow<ApiCallResult<Map<String, PingiItem>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getPingiSwapPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }


    override fun getWallex(market: String): Flow<ApiCallResult<WallexMarkets>> = flow {
        val response = getResult {
            marketRemoteDataSource.getWallexSwapPrice(market)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getSaraf(): Flow<ApiCallResult<SarafPrice>> = flow {
        val response = getResult {
            marketRemoteDataSource.getSarafSwapPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getArz3Price(): Flow<ApiCallResult<List<Arz3CoinItem>?>> = flow {
        val response = getResult {
            marketRemoteDataSource.getArz3SwapPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

    override fun getUbitexPrice(
        baseCurrency: String,
        quoteCurrency: String,
    ): Flow<ApiCallResult<Ubitex?>> = flow {
        val response = getResult {
            marketRemoteDataSource.getUbitexSwapPrice(baseCurrency, quoteCurrency)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }

}