package com.numtory.application.features.market.domain.entities


data class Ubitex constructor(
    var buy: UbitexPrice?,
    var sell: UbitexPrice?
) {

}

data class UbitexPrice constructor(
    var price: String?,
) {

}

