package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.TalaSea

data class TalaSeaDataModel(
    @SerializedName("price")
    val price: String?,
) {
    fun toEntity(): TalaSea = TalaSea(price)
}

