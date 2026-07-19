package com.numtory.application.features.notification.data.models

import com.google.firebase.messaging.RemoteMessage
import com.numtory.application.features.notification.data.PushConstants
import com.numtory.application.features.notification.domain.entities.PushMessage

fun RemoteMessage.toPushMessage(): PushMessage? {
    val title = notification?.title ?: data[PushConstants.KEY_TITLE]
    val body = notification?.body ?: data[PushConstants.KEY_BODY]

    if (title.isNullOrBlank() && body.isNullOrBlank()) return null

    return PushMessage(
        title = title.orEmpty(),
        body = body.orEmpty(),
        route = data[PushConstants.KEY_ROUTE]?.takeIf { it.isNotBlank() }
    )
}
