package com.numtory.application.features.goldExchange.domain.entities

data class MelliGold(
    val data: MelliGoldPrice?,
) {
}

data class MelliGoldPrice(
    val buy: Long?,
    val sell: Long?,
) {
}