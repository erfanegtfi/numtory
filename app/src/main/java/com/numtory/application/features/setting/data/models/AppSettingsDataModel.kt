package com.numtory.application.features.setting.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.setting.domain.entities.AppSettings

data class AppSettingsDataModel(
    @SerializedName("version")
    val version: Int,
    @SerializedName("force")
    val force: Boolean
) {
    fun toEntity(): AppSettings =
        AppSettings(version, force)
}


