package com.numtory.application.features.market.data.local

import com.numtory.application.features.market.domain.enums.Exchanges

interface LocalDataRepository {
    fun saveEnableExchanges(exchanges: List<Exchanges>?)
    fun getEnableExchanges(): List<Exchanges>?

    fun saveAddFee(addFee: Boolean?)
    fun addFee(): Boolean
}