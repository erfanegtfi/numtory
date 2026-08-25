package com.numtory.application.features.cryptoExchange.domain.entities

data class WallexResults(
    val results: WallexMarkets
)

data class WallexMarkets constructor(
    val results: List<WallexMarketItem>
)

data class WallexMarketItem constructor(
    val baseAsset: String,
    val quotes: Map<String, WallexQuotePrice>
)

data class WallexQuotePrice constructor(
    var price: String?,
)


