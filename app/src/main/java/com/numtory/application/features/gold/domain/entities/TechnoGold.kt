package com.numtory.application.features.gold.domain.entities

data class TechnoGold(
    val data: TechnoGoldPrice?,
) {
}

data class TechnoGoldPrice(
    val sellPrice: Int?,
    val buyPrice: Int?,
) {
}