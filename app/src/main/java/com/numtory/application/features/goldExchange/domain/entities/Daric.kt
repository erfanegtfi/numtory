package com.numtory.application.features.goldExchange.domain.entities


data class Daric(
    val bestBuy: DaricPrice?,
    val bestSell: DaricPrice?,
) {
}

data class DaricPrice(
    val price: Long,
) {
}
