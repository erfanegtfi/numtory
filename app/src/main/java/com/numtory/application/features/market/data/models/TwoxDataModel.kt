package com.numtory.application.features.market.data.models

import com.numtory.application.features.market.domain.entities.Twox
import com.google.gson.annotations.SerializedName

data class TwoxDataModel(
    @SerializedName("displayFee")
    val displayFee: String
) {
    fun toEntity(): Twox = Twox(displayFee)
}