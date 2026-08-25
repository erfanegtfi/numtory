package com.numtory.application.features.cryptoExchange.domain.entities


data class Exonyx(
    val data: List<ExonyxItem>
) {
}

data class ExonyxItem constructor(
    var iso: String?,
    var priceBuy: String?,
    var priceSell: String?,

) {

}

