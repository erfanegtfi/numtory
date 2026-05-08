package com.numtory.application.features.market.domain.entities

import com.numtory.application.features.market.domain.enums.Exchanges


data class ExchangeStatus constructor(
    var id: Integer,
    var exchange: Exchanges?,
    var display: Boolean,
    var active: Boolean,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExchangeStatus) return false

        return exchange == other.exchange

    }

    override fun hashCode(): Int {
        return 31 * exchange.hashCode()
    }

}