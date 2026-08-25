package com.numtory.application.features.cryptoExchange.domain.entities

data class Pooleno constructor(
//    var rate: String?,
    var payload: PoolenoPayload?,
) {

}

data class PoolenoPayload constructor(
    var rate: String?,
) {

}