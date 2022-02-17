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

    companion object {
        var started = false
    }

    private lateinit var notification: Notification
    private val ONGOING_NOTIFICATION_ID: Int = 77

    val NOTIFICATION_CHANNEL_ID = "com.coryroy.servicesexample"
    val channelName = "Foreground Service Example"
    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        channelName,
        NotificationManager.IMPORTANCE_DEFAULT
    )

    var countingJob: Job? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            intent.let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)!!
        manager.createNotificationChannel(channel)

        notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getString(R.string.x_count, CountingViewModel.count.value ?: 0))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setTicker(getString(R.string.x_count, CountingViewModel.count.value ?: 0))
            .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)

        countingJob = startCounting()
        started = true

        return START_STICKY
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

                Log.d("StartedCountingSVC", "$newCount")
                CountingViewModel.count.postValue(newCount)
            }
        }
    }

    override fun onDestroy() {
        countingJob?.cancel()
        started = false
        super.onDestroy()
    }
}

