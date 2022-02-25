package com.coryroy.servicesexample.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.graphics.Color
import com.coryroy.servicesexample.R

class WorkerNotification(private val context: Context, title: String) {

    companion object {
        private const val NOTIFICATION_ID: Int = 78
        private const val NOTIFICATION_CHANNEL_ID = "com.coryroy.servicesexample.work"
    }

    private val channelName = "Work Example"

    private val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        channelName,
        NotificationManager.IMPORTANCE_DEFAULT
    )

    private fun addNotificationChannel() {
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = (context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(channel)
    }

    init {
        addNotificationChannel()
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setTimeoutAfter(10000)
            .build()

        val manager = (context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager)
        manager.cancel(NOTIFICATION_ID)
        manager.notify(NOTIFICATION_ID, notification)
    }
}