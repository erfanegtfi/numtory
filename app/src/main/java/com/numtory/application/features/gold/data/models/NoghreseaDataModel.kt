package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.NoghreSea

data class NoghreSeaDataModel(
    @SerializedName("price")
    val price: String?,
) {
    fun toEntity(): NoghreSea = NoghreSea(price)
}

