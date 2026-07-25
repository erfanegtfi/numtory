package com.numtory.application.features.market.domain.entities

/**
 * The best price on each side of the market, together with the exchange that quoted it.
 * A price is null while no exchange has quoted a usable one, and its exchange is null with it.
 */
data class BestPrices(
    val buyPrice: Double? = null,
    val buyExchange: String? = null,
    val sellPrice: Double? = null,
    val sellExchange: String? = null,
)
