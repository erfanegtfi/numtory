package com.numtory.application.features.gold.domain.entities

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.gold.domain.entities.Digikala
import com.numtory.application.features.gold.domain.entities.DigikalaPrice
import com.numtory.application.features.gold.domain.entities.Goldika
import com.numtory.application.features.gold.domain.entities.GoldikaData
import com.numtory.application.features.gold.domain.entities.GoldikaPrice

data class MelliGold(
    val data: MelliGoldPrice?,
) {
}

data class MelliGoldPrice(
    val buy: Int?,
    val sell: Int?,
) {
}