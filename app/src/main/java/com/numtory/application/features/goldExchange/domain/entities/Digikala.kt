package com.numtory.application.features.goldExchange.domain.entities

data class Digikala(
    val gold18: DigikalaPrice?,
) {
}

data class DigikalaPrice(
    val price: Long,
) {
}
