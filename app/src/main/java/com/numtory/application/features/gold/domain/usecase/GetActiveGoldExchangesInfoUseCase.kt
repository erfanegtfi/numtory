package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo

class GetActiveGoldExchangesInfoUseCase constructor(
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
) {

    fun action(): List<GoldExchangeInfo> =
        exchangesLocalDataSource.getGoldExchangesInfo()
            ?.filter { it.active && it.display }
            ?: emptyList()
}
