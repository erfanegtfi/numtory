package com.numtory.application.features.market.domain.entities


data class Arzypto constructor(
    var data: ArzyptoPrice?,

    ) {

}

data class ArzyptoPrice constructor(
    var symbolTomanPrice: String?,
) {

}