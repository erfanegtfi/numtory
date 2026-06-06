package com.numtory.application.features.setting.domain.entities

data class AppSettings(
    val version: Int?,
    val versionName: String?,
    val force: Boolean?,
    val block: Boolean?,
) {
}


