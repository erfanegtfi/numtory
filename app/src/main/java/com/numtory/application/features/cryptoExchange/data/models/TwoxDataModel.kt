package com.numtory.application.features.cryptoExchange.data.models

import com.numtory.application.features.cryptoExchange.domain.entities.Twox
import com.google.gson.annotations.SerializedName

data class TwoxDataModel(
    @SerializedName("displayFee")
    val displayFee: String?
) {
    fun toEntity(): Twox = Twox(displayFee)
}