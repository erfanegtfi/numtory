package com.numtory.application.features.cryptoExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.cryptoExchange.domain.entities.Ramzinex
import com.numtory.application.features.cryptoExchange.domain.entities.RamzinexPrice

data class RamzinexCurrenciesDataModel constructor(
    @SerializedName("data")
    var data: RamzinexCurrencyDataModel?,

    ) {

}

data class RamzinexCurrencyDataModel constructor(
    @SerializedName("currencies")
    var currencies: List<RamzinexCurrencyItemDataModel>?,

    ) {


}

data class RamzinexCurrencyItemDataModel constructor(
    @SerializedName("symbol")
    var symbol: String?,
    @SerializedName("id")
    var id: Int?,
    ) {

}

data class RamzinexDataModel constructor(
    @SerializedName("data")
    var data: RamzinexPriceDataModel?,

    ) {

    fun toEntity(): Ramzinex =
        Ramzinex(data?.toEntity())
}

data class RamzinexPriceDataModel constructor(
    @SerializedName("from_amount")
    var fromAmount: String?,
    @SerializedName("to_amount")
    var toAmount: String?
) {

    fun toEntity(): RamzinexPrice =
        RamzinexPrice(fromAmount, toAmount)
}