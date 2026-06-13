package com.numtory.application.features.market.domain.entities

data class Bitbarg constructor(
    var result: BitbargItems?,

    ) {
}

data class BitbargItems constructor(
    var items: List<BitbargItem>?,
) {

}

data class BitbargItem constructor(
    var price: String?,
    var slug: String?,
) {

}