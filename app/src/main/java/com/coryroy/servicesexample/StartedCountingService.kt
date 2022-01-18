package com.coryroy.servicesexample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class StartedCountingService : Service() {

    private val viewModel = CountingViewModel

    var countingJob: Job? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        countingJob = startCounting()

        return START_STICKY
    }

    private fun startCounting(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while(countingJob?.isActive == true) {
                delay(1000)
                val newCount = (viewModel.count.value ?: 0) + 1

                Log.d("CountingService", "$newCount")
                viewModel.count.postValue(newCount)
            }
        }
    }

    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }
}