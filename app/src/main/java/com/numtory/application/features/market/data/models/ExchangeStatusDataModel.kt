package com.numtory.application.features.market.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.market.domain.entities.ExchangeStatus
import com.numtory.application.features.market.domain.enums.Exchanges
import kotlinx.serialization.Serializable


@Serializable
data class ExchangeStatusDataModel constructor(
    @SerializedName("id")
    var id: Integer,
    @SerializedName("name")
    var name: String,
    @SerializedName("display")
    var display: Boolean,
    @SerializedName("active")
    var active: Boolean,
) {

    fun toEntity(): ExchangeStatus =
        ExchangeStatus(id, Exchanges.fromString(name) , display, active)
}