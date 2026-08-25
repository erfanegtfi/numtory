package com.numtory.application.features.goldExchange.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.goldExchange.domain.entities.NoghreSea

data class NoghreSeaDataModel(
    @SerializedName("price")
    val price: String?,
) {
    fun toEntity(): NoghreSea = NoghreSea(price)
}

