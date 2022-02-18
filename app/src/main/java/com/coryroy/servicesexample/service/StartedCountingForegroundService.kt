package com.coryroy.servicesexample.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import com.coryroy.servicesexample.R
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*


class StartedCountingForegroundService : Service() {

    var countingJob: Job? = null

    companion object {
        private const val ONGOING_NOTIFICATION_ID: Int = 77
        private const val NOTIFICATION_CHANNEL_ID = "com.coryroy.servicesexample"
        var started = false
    }

    private lateinit var notification: Notification

    private val channelName = "Foreground Service Example"
    private val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        channelName,
        NotificationManager.IMPORTANCE_DEFAULT
    )

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        started = true

        generateNotification()
        startForeground(ONGOING_NOTIFICATION_ID, notification)
        countingJob = startCounting()

        return START_STICKY
    }

    private fun generateNotification() {
        notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getString(R.string.x_count, CountingViewModel.count.value ?: 0))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setTicker(getString(R.string.x_count, CountingViewModel.count.value ?: 0))
            .build()
    }

    private fun addNotificationChannel() {
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(channel)
    }

    override fun onStart(intent: Intent?, startId: Int) {
        addNotificationChannel()
    }

    override fun stopService(name: Intent?): Boolean {
        started = false
        countingJob?.cancel()
        return super.stopService(name)
    }

    private fun startCounting(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while(countingJob?.isActive == true) {
                delay(1000)
                val newCount = (CountingViewModel.count.value ?: 0) + 1
                CountingViewModel.count.postValue(newCount)
                val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                generateNotification()
                manager.notify(ONGOING_NOTIFICATION_ID, notification)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        countingJob?.cancel()
        started = false
        super.onDestroy()
    }
}

