package com.numtory.application.features.goldExchange.domain.entities


data class WallGold(
    val result: WallGoldPrice?,
) {
}

data class WallGoldPrice(
    val price: String?,
) {
}