package com.numtory.application.features.gold.domain.entities

data class Digikala(
    val gold18: DigikalaPrice?,
) {
}

data class DigikalaPrice(
    val price: Int,
) {
}
