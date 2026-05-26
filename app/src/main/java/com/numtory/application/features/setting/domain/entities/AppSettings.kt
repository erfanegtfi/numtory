package com.numtory.application.features.setting.domain.entities

import com.numtory.application.features.market.domain.entities.WallexMarkets
import com.google.gson.annotations.SerializedName

data class AppSettings(
    val version: Int,
    val force: Boolean
) {
}


