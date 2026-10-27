package com.numtory.application.features.setting.data.models

import com.google.gson.annotations.SerializedName
import com.numtory.application.features.setting.domain.entities.AppSettings

data class AppSettingsDataModel(
    @SerializedName("version")
    val version: Int?,
    @SerializedName("force")
    val force: Boolean?,
    @SerializedName("version_name")
    val versionName: String?,
    @SerializedName("fiat")
    val fiat: Boolean?,
    @SerializedName("car")
    val car: Boolean?,
    @SerializedName("block")
    val block: Boolean?
) {
    fun toEntity(): AppSettings =
        AppSettings(version, versionName, force, block,fiat, car)
}


