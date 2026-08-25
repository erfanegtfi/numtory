package com.numtory.application.features.goldExchange.domain.entities


data class EcoGold(
    val data: List<EcoGoldPrice>?,
) {
}

data class EcoGoldPrice(
    val symbol: String?,
    val sellPrice: String?,
    val buyPrice: String?,
) {
}