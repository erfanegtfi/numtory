package com.numtory.application.features.gold.data.repositories

import com.numtory.application.data.remote.getResult
import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.gold.data.dataSource.GoldMarketRemoteDataSource
import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.data.models.HamrahGoldDataModel
import com.numtory.application.features.gold.data.models.MelliGoldDataModel
import com.numtory.application.features.gold.data.models.MilliDataModel
import com.numtory.application.features.gold.data.models.TlynDataModel
import com.numtory.application.features.gold.data.models.WallGoldDataModel
import com.numtory.application.features.gold.domain.entities.Daric
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.Goldika
import com.numtory.application.features.gold.domain.entities.HamrahGold
import com.numtory.application.features.gold.domain.entities.MelliGold
import com.numtory.application.features.gold.domain.entities.Milli
import com.numtory.application.features.gold.domain.entities.TalaSea
import com.numtory.application.features.gold.domain.entities.TechnoGold
import com.numtory.application.features.gold.domain.entities.Tlyn
import com.numtory.application.features.gold.domain.entities.WallGold
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


interface GoldMarketRepository {
    fun getExchanges(): Flow<ApiCallResult<List<GoldExchangeInfo>>>
    fun getSavedExchangesInfo(): List<GoldExchangeInfo>?
    fun getDigikalaPrice(): Flow<ApiCallResult<Digikala>>
    fun getGoldikaPrice(): Flow<ApiCallResult<Goldika>>
    fun getHamrahGoldPrice(): Flow<ApiCallResult<HamrahGold>>
    fun getTlynPrice(): Flow<ApiCallResult<Tlyn>>
    fun getMelliGoldPrice(): Flow<ApiCallResult<MelliGold>>
    fun getTalaseaPrice(): Flow<ApiCallResult<TalaSea>>
    fun getWallGoldPrice(): Flow<ApiCallResult<WallGold>>
    fun getMilliPrice(): Flow<ApiCallResult<Milli>>
    fun getTechnoGoldPrice(): Flow<ApiCallResult<TechnoGold>>
    fun getDaricPrice(symbol: String): Flow<ApiCallResult<Daric>>
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

    override fun getHamrahGoldPrice(): Flow<ApiCallResult<HamrahGold>> = flow {
        val response = getResult {
            marketRemoteDataSource.getHamrahGoldPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getTlynPrice(): Flow<ApiCallResult<Tlyn>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTlynPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getMelliGoldPrice(): Flow<ApiCallResult<MelliGold>> = flow {
        val response = getResult {
            marketRemoteDataSource.getMelliGoldPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getTalaseaPrice(): Flow<ApiCallResult<TalaSea>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTalaseaPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getWallGoldPrice(): Flow<ApiCallResult<WallGold>> = flow {
        val response = getResult {
            marketRemoteDataSource.getWallGoldPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getMilliPrice(): Flow<ApiCallResult<Milli>> = flow {
        val response = getResult {
            marketRemoteDataSource.getMilliPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


    override fun getTechnoGoldPrice(): Flow<ApiCallResult<TechnoGold>> = flow {
        val response = getResult {
            marketRemoteDataSource.getTechnoGoldPrice()
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)

    override fun getDaricPrice(symbol: String): Flow<ApiCallResult<Daric>> = flow {
        val response = getResult {
            marketRemoteDataSource.getDaricPrice(symbol)
        }
        if (response is ApiCallResult.Success) {
            emit(ApiCallResult.Success(response.result.toEntity()))
        } else if (response is ApiCallResult.Failure)
            emit(ApiCallResult.Failure(response.error))
    }.flowOn(dispatcher)


}