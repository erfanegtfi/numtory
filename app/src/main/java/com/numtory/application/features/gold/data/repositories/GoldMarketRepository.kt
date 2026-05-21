package com.numtory.application.features.gold.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.dataSource.GoldMarketRemoteDataSource
import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.data.models.DigikalaDataModel
import com.numtory.application.features.gold.data.models.GoldikaDataModel
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.Goldika
import com.numtory.application.features.gold.domain.entities.GoldikaPrice
import com.numtory.application.features.market.data.dataSource.MarketRemoteDataSource
import com.numtory.application.features.market.data.local.ExchangesLocalDataSource
import com.numtory.application.features.market.domain.entities.AbanTether
import com.numtory.application.features.market.domain.entities.Arz3CoinItem
import com.numtory.application.features.market.domain.entities.ArzinjaItem
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
import com.numtory.application.features.market.domain.entities.Ramzinex
import com.numtory.application.features.market.domain.entities.SarafPrice
import com.numtory.application.features.market.domain.entities.SarmayexMarketItem
import com.numtory.application.features.market.domain.entities.SarmayexSwap
import com.numtory.application.features.market.domain.entities.TabdealMarketItem
import com.numtory.application.features.market.domain.entities.TabdealSwap
import com.numtory.application.features.market.domain.entities.TetherLand
import com.numtory.application.features.market.domain.entities.Twox
import com.numtory.application.features.market.domain.entities.Ubitex
import com.numtory.application.features.market.domain.entities.WallexMarkets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface GoldMarketRepository {
    fun getExchanges(): Flow<ApiCallResult<List<GoldExchangeInfo>>>
    fun getSavedExchangesInfo(): List<GoldExchangeInfo>?
    fun getDigikalaPrice(): Flow<ApiCallResult<Digikala>>
    fun getGoldikaPrice(): Flow<ApiCallResult<Goldika>>

}

class GoldMarketRepositoryImpl(
    private val marketRemoteDataSource: GoldMarketRemoteDataSource,
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) : GoldMarketRepository {

    override fun getExchanges(): Flow<ApiCallResult<List<GoldExchangeInfo>>> = flow {
        val response = getResult {
            marketRemoteDataSource.getGoldExchanges()
        }
        if (response is ApiCallResult.Success) {
            exchangesLocalDataSource.saveGoldExchangesInfo(response.result)
            emit(ApiCallResult.Success(response.result.map { it.toEntity() }))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getSavedExchangesInfo(): List<GoldExchangeInfo>? {
        return exchangesLocalDataSource.getGoldExchangesInfo()
    }

    override fun getDigikalaPrice(): Flow<ApiCallResult<Digikala>> = flow {
        val response = getResult {
            marketRemoteDataSource.getDigikalaPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getGoldikaPrice(): Flow<ApiCallResult<Goldika>> = flow {
        val response = getResult {
            marketRemoteDataSource.getGoldikaPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


}