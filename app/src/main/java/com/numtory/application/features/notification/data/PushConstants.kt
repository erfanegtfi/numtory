package com.numtory.application.features.notification.data

object PushConstants {

    const val CHANNEL_ID = "numtory_general"

    /**
     * Data-payload keys. [KEY_ROUTE] doubles as the intent-extra key: when a payload carries a
     * `notification` block and the app is backgrounded, FCM builds the tray notification itself and
     * copies every data key onto the launch intent, so reading the same name on both paths lets
     * foreground and background taps route identically.
     */
    const val KEY_TITLE = "title"
    const val KEY_BODY = "body"
    const val KEY_ROUTE = "route"
}
