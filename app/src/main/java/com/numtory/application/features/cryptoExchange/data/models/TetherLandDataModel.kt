package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.TetherLand
import com.google.gson.annotations.SerializedName

data class TetherLandListDataModel(
    @SerializedName("data")
    val data: List<TetherLandDataModel>

) {
    fun toEntity(): List<TetherLand> =
        data.map { it.toEntity() }
}

data class TetherLandDataModel constructor(
    @SerializedName("toman_amount")
    var tomanAmount: String?,
    @SerializedName("symbol")
    var symbol: String?
) {

    fun toEntity(): TetherLand =
        TetherLand(tomanAmount, symbol)
}