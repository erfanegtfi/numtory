package com.numtory.application.features.gold.domain.entities


data class Zarafza(
    val data: ZarafzaData?,
) {
}

data class ZarafzaData(
    val gold18: ZarafzaGold?,
) {
}

data class ZarafzaGold(
    val sell: ZarafzaPrice?,
    val buy: ZarafzaPrice?,
) {
}

data class ZarafzaPrice(
    val price: Double?,
) {
}
