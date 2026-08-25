package com.numtory.application.features.goldExchange.domain.entities


data class Gerami(
    val pairs: List<GeramiPair>?,
) {
}

data class GeramiPair(
    val symbol: String?,
    val type: String?,
    val sellPrice: String?,
    val buyPrice: String?,
) {
}
