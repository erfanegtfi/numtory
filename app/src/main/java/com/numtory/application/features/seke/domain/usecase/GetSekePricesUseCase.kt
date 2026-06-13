package com.numtory.application.features.seke.domain.usecase

import com.numtory.application.data.utils.ApiCallResult
import com.numtory.application.features.cryptoMarket.data.repositories.CryptoGlobalMarketRepository
import com.numtory.application.features.cryptoMarket.domain.entities.CryptoMarketPrice
import com.numtory.application.features.seke.data.repositories.SekeRepository
import com.numtory.application.features.seke.domain.entities.SekePrice
import kotlinx.coroutines.flow.Flow

class GetSekePricesUseCase constructor(
    private val marketRepository: SekeRepository,
) {

    fun action(): Flow<ApiCallResult<List<SekePrice>>> {
        return marketRepository.getSekePrices()
    }
}

