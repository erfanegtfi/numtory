package com.numtory.application.features.setting.data.dataSource

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import com.numtory.application.BuildConfig
import com.numtory.application.features.setting.data.models.AppSettingsDataModel

interface SettingsRemoteDataSource {
    suspend fun getAppSettings(): AppSettingsDataModel

}

class SettingsRemoteDataSourceImpl constructor(
    private val httpClient: HttpClient,
    private val gson: Gson
) : SettingsRemoteDataSource {

    override suspend fun getAppSettings(): AppSettingsDataModel {
        val response =
            httpClient.get(BuildConfig.APP_SETTINGS_URL)
        val json = response.bodyAsText()
        return gson.fromJson(json, AppSettingsDataModel::class.java)
    }
}