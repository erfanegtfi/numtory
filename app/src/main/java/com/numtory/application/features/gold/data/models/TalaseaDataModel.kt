package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.TalaSea

data class TalaSeaDataModel(
    @SerializedName("price")
    val price: String?,
) {
    fun toEntity(): TalaSea = TalaSea(price)
}

