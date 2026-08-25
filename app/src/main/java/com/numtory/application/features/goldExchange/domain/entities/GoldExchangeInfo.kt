package com.numtory.application.features.goldExchange.domain.entities

import com.numtory.application.features.goldExchange.domain.enums.GoldExchanges


data class GoldExchangeInfo constructor(
    var id: Integer? = null,
    var exchange: GoldExchanges,
    var display: Boolean,
    var active: Boolean,
    var isMarket: Boolean? = null,
    var hasMarket: Boolean? = null,
    var fee: Float? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GoldExchangeInfo) return false

        return exchange == other.exchange

    }

    override fun hashCode(): Int {
        return 31 * exchange.hashCode()
    }

}