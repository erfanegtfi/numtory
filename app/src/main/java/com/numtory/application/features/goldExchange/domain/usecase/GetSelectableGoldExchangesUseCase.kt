package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.goldExchange.domain.entities.GoldExchangeInfo

class GetSelectableGoldExchangesUseCase constructor(
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
) {

    fun action(): List<GoldExchangeInfo> =
        exchangesLocalDataSource.getGoldExchangesInfo()
            ?.filter { it.active && it.display }
            ?: emptyList()
}
