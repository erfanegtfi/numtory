package com.numtory.application.features.cryptoExchange.domain.entities

data class Bit24MarketList(
    val results: List<Bit24>
) {

}

data class Bit24 constructor(
    var name: String?,
    var faName: String?,
    var symbol: String?,
    var market: Map<String, Bit24Price>?
) {

}

data class Bit24Price constructor(
    var price: String?,
) {

}
/////// swap

data class Bit24Swap(
    val data: Bit24Metas?
) {

}

data class Bit24Metas(
    val metas: Bit24MetaPrice?
) {
}

data class Bit24MetaPrice constructor(
    var sellPrice: String?,
    var buyPrice: String?,
) {


}