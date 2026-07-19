package com.numtory.application.features.notification.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.numtory.application.features.notification.data.NotificationPresenter
import com.numtory.application.features.notification.data.models.toPushMessage
import com.numtory.application.features.notification.data.repositories.PushTokenRepository
import org.koin.android.ext.android.inject

class NumtoryMessagingService : FirebaseMessagingService() {

    private val notificationPresenter: NotificationPresenter by inject()
    private val pushTokenRepository: PushTokenRepository by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pushTokenRepository.saveToken(token)
    }

    /**
     * Only called when the app is foregrounded, or for data-only payloads. A payload carrying a
     * `notification` block while backgrounded is rendered by FCM itself and never reaches here.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val push = message.toPushMessage() ?: return
        notificationPresenter.show(push)
    }
}
