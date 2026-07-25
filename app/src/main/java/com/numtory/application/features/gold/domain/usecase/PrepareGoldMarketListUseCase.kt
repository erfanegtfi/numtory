package com.numtory.application.features.gold.domain.usecase

import com.numtory.application.features.gold.domain.entities.GoldExchangeInfo
import com.numtory.application.features.gold.domain.entities.GoldMarketPrice
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.features.market.domain.enums.FilterBy
import com.numtory.application.features.market.domain.enums.SortField
import com.numtory.application.features.market.domain.enums.SortOrder

class PrepareGoldMarketListUseCase constructor(
    private val filterMarketUseCase: FilterGoldMarketUseCase,
    private val sortMarketUseCase: SortGoldMarketUseCase,
) {

    fun action(params: PrepareGoldMarketListParams): List<GoldMarketPrice> {
        val filtered = filterMarketUseCase.action(
            FilterGoldParams(
                filter = params.filter,
                userExchanges = params.userExchanges,
                exchangesInfo = params.exchangesInfo,
                markets = params.markets,
                addFee = params.addFee,
            )
        )

        return sortMarketUseCase.action(
            SortGoldParams(
                sortField = params.sortField,
                sortOrder = params.sortOrder,
                markets = filtered,
            )
        )
    }
}

data class PrepareGoldMarketListParams(
    val markets: List<GoldMarketPrice>,
    val userExchanges: List<GoldExchanges>,
    val exchangesInfo: List<GoldExchangeInfo>?,
    val addFee: Boolean,
    val sortField: SortField,
    val sortOrder: SortOrder,
    val filter: FilterBy = FilterBy.All,
)
