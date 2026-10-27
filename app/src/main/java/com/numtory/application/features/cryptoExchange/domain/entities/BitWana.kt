package com.numtory.application.features.cryptoExchange.domain.entities


data class BitWana constructor(
    var content: BitWanaContent?,

    ) {

}

data class BitWanaContent constructor(
    var buyPrice: String?,
    var sellPrice: String?,
) {

}