package com.numtory.application.features.cryptoExchange.domain.entities


data class Arzypto constructor(
    var data: ArzyptoPrice?,

    ) {

}

data class ArzyptoPrice constructor(
    var symbolTomanPrice: String?,
) {

}