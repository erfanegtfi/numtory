package com.numtory.application.features.gold.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.DigikalaPrice

data class DigikalaDataModel(
    @SerializedName("gold18")
    val gold18: DigikalaPriceDataModel?,
) {
    fun toEntity(): Digikala = Digikala(gold18?.toEntity())
}

data class DigikalaPriceDataModel(
    @SerializedName("price")
    val price: Int,
) {
    fun toEntity(): DigikalaPrice = DigikalaPrice(price)
}
