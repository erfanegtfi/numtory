package com.numtory.application.features.market.domain.entities

data class Pooleno constructor(
    var payload: PoolenoPayload?,
) {

}

data class PoolenoPayload constructor(
    var rate: String?,
) {

}