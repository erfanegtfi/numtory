package com.numtory.application.features.goldExchange.domain.entities


data class Tlyn(
    val prices: List<List<TlynData>?>?,

    ) {
}

data class TlynData(
    val symbol: String,
    val price: TlynPrice?,

    ) {
}

data class TlynPrice(
    val sell: Long,
    val buy: Long,
) {
}