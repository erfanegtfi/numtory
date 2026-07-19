package com.numtory.application.features.notification.data.repositories

import com.numtory.application.features.notification.data.local.PushTokenLocalDataSource

interface PushTokenRepository {
    fun getToken(): String?
    fun saveToken(token: String)
}

class PushTokenRepositoryImpl(
    private val localDataSource: PushTokenLocalDataSource
) : PushTokenRepository {

    override fun getToken(): String? = localDataSource.getToken()

    override fun saveToken(token: String) {
        localDataSource.saveToken(token)
    }
}
