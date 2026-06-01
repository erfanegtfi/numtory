package com.numtory.application.features.market.domain.entities


data class BitPinOTC constructor(
    var sell: String?,
    var buy: String?
) {


}

data class BitPin constructor(
    var id: Int?,
    var code: String?,
    var results: BitPinPriceInfo?,
) {

}

data class BitPinPriceInfo constructor(
    var price: String?,
) {
}