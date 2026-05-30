package com.numtory.application.features.cryptoMarket.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import kotlinx.coroutines.flow.Flow

class GetCryptoGlobalMarketPricesUseCase constructor(
    private val marketRepository: CryptoGlobalMarketRepository,
) {

    fun action(): Flow<ApiCallResult<List<CryptoMarketPrice>>> {
        return marketRepository.getCryptoPrices()
    }
}

