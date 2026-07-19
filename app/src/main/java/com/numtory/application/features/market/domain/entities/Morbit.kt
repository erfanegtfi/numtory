package com.numtory.application.features.market.domain.entities


data class Morbit(
    val data: List<MorbitItem>?
) {
}

data class MorbitItem constructor(
    var instrument: String?,
    var buyPrice: String?,
    var sellPrice: String?,
    /** Quote currency the two prices are expressed in — "irt" or "usdt". */
    var unit: String?,
) {

}
