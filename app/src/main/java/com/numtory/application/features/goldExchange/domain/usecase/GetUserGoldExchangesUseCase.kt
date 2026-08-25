package com.numtory.application.features.goldExchange.domain.usecase

import com.numtory.application.features.goldExchange.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges

class GetUserGoldExchangesUseCase constructor(
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
) {

    fun action(): List<GoldExchanges> =
        exchangesLocalDataSource.getUserGoldExchanges() ?: GoldExchanges.entries
}
