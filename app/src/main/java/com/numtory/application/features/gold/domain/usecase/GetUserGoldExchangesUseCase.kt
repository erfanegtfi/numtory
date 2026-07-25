package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.data.local.GoldExchangesLocalDataSource
import com.numtory.application.features.gold.domain.enums.GoldExchanges

class GetUserGoldExchangesUseCase constructor(
    private val exchangesLocalDataSource: GoldExchangesLocalDataSource,
) {

    fun action(): List<GoldExchanges> =
        exchangesLocalDataSource.getUserGoldExchanges() ?: GoldExchanges.entries
}
