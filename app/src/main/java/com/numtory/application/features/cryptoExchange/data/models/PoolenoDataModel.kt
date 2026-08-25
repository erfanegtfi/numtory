package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.Pooleno
import com.numtory.application.features.cryptoExchange.domain.entities.PoolenoPayload
import com.google.gson.annotations.SerializedName


data class PoolenoDataModel constructor(
//    @SerializedName("rate")
//    var rate: String?,
    @SerializedName("payload")
    var payload: PoolenoPayloadDataModel?,
) {

    fun toEntity(): Pooleno =
        Pooleno(payload?.toEntity())
}

data class PoolenoPayloadDataModel constructor(
    @SerializedName("rate")
    var rate: String?,
) {

    fun toEntity(): PoolenoPayload =
        PoolenoPayload(rate)
}