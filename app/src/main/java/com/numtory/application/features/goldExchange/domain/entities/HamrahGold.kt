package com.numtory.application.features.goldExchange.domain.entities

//data class HamrahGold(
//    val buy: List<Long>?,
//    val sell: List<Long>?,
//)

data class HamrahGold(
    val data: HamrahGoldData?,
)


data class HamrahGoldData(
    val current: Long?,
)
