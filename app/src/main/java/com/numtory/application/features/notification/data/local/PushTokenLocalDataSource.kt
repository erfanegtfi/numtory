package com.numtory.application.features.notification.data.local

import com.numtory.application.data.local.preferences.PreferencesConstants
import com.numtory.application.data.local.preferences.Session

interface PushTokenLocalDataSource {
    fun getToken(): String?
    fun saveToken(token: String)
}

class PushTokenLocalDataSourceImpl(
    private val session: Session
) : PushTokenLocalDataSource {

    override fun getToken(): String? =
        session.getPreferenceValue(PreferencesConstants.PUSH_TOKEN, null)

    override fun saveToken(token: String) {
        session.setPreferenceValue(PreferencesConstants.PUSH_TOKEN, token)
    }
}
