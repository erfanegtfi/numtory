package com.numtory.application.features.market.domain.entities


data class Ramzinex constructor(
    var data: RamzinexPrice?,

    ) {
}

data class RamzinexPrice constructor(
    var fromAmount: String?,
    var toAmount: String?
) {

}