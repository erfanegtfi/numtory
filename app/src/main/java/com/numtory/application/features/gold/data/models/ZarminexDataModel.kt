package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Daric
import com.numtory.application.features.gold.domain.entities.DaricPrice
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.DigikalaPrice
import com.numtory.application.features.gold.domain.entities.Zarminex

data class ZarminexDataModel(
    @SerializedName("buy")
    val buy: Long?,
    @SerializedName("sell")
    val sell: Long?,
) {
    fun toEntity(): Zarminex = Zarminex(buy, sell)
}

