package com.numtory.application.features.gold.domain.entities

data class Goldika(
    val data: GoldikaData?,
) {
}

data class GoldikaData(
    val price: GoldikaPrice?,
) {
}

data class GoldikaPrice(
    val sell: Long,
    val buy: Long,
) {
}
