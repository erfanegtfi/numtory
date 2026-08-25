package com.numtory.application.features.goldExchange.domain.entities

data class TechnoGold(
    val data: TechnoGoldPrice?,
) {
}

data class TechnoGoldPrice(
    val sellPrice: Long?,
    val buyPrice: Long?,
) {
}