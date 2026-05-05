package com.numtory.application.features.market.domain.entities

data class Pingi(
    val stats: Map<String, PingiItem>
) {

}

data class PingiItem constructor(
    var price: String?,
    var market: String?,
) {


}