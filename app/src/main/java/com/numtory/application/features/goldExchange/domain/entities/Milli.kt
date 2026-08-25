package com.numtory.application.features.goldExchange.domain.entities

data class Milli(
    val data: MilliPrice?,
) {
}

data class MilliPrice(
    val price: Long?,
) {
}