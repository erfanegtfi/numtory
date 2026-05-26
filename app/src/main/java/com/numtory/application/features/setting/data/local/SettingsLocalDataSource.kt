package com.numtory.application.features.setting.data.local

import com.google.gson.Gson
import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session
import com.numtory.application.features.gold.domain.enums.GoldExchanges
import com.numtory.application.features.setting.data.models.AppSettingsDataModel
import com.numtory.application.features.setting.domain.entities.AppSettings
import kotlinx.serialization.json.Json

interface SettingsLocalDataSource {

    fun saveAppSettings(appSettings: AppSettingsDataModel?)
    fun getAppSettings(): AppSettingsDataModel?
}

class SettingsLocalDataSourceImpl constructor(
    private val session: Session,
) : SettingsLocalDataSource {


    override fun saveAppSettings(appSettings: AppSettingsDataModel?) {
        val json = Gson().toJson(appSettings)
        session.setPreferenceValue(PreferencesConstants.APP_SETTINGS, json)
    }

    override fun getAppSettings(): AppSettingsDataModel? =
        session.getObject(PreferencesConstants.APP_SETTINGS, AppSettingsDataModel::class.java)


}