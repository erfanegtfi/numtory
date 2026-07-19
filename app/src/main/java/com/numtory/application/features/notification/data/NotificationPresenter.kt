package com.numtory.application.features.notification.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.numtory.application.MainActivity
import com.numtory.application.R
import com.numtory.application.features.notification.domain.entities.PushMessage
import java.util.concurrent.atomic.AtomicInteger

class NotificationPresenter(
    private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val notificationId = AtomicInteger(BASE_NOTIFICATION_ID)

    fun createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            PushConstants.CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun show(push: PushMessage) {
        if (!canPostNotifications()) return

        createChannel()

        val notification = NotificationCompat.Builder(context, PushConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.notification_color))
            .setContentTitle(push.title)
            .setContentText(push.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(push.body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentIntent(push))
            .build()

        notificationManager.notify(notificationId.getAndIncrement(), notification)
    }

    private fun contentIntent(push: PushMessage): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            push.route?.let { putExtra(PushConstants.KEY_ROUTE, it) }
        }

        var flags = PendingIntent.FLAG_UPDATE_CURRENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags = flags or PendingIntent.FLAG_IMMUTABLE
        }

        // Distinct request code per route, otherwise FLAG_UPDATE_CURRENT would reuse one
        // PendingIntent and every notification would carry the most recent route's extras.
        return PendingIntent.getActivity(context, push.route.hashCode(), intent, flags)
    }

    /**
     * On API 33+ notify() silently no-ops without POST_NOTIFICATIONS. Checking here keeps the
     * pre-33 path (where the permission does not exist) working off areNotificationsEnabled alone.
     */
    private fun canPostNotifications(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return false
        }
        return notificationManager.areNotificationsEnabled()
    }

    private companion object {
        const val BASE_NOTIFICATION_ID = 1000
    }
}
