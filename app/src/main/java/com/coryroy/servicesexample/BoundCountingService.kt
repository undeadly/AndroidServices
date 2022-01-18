package com.coryroy.servicesexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class BoundCountingService : Service(){

    private val viewModel = CountingViewModel

    private val binder = CountingBinder()

    private var countingJob: Job? = null

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    fun startCounting() {
       countingJob = startCountingJob()
    }

    private fun startCountingJob() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (countingJob?.isActive != false) {
                delay(1000)
                val newCount = (viewModel.count.value ?: 0) + 1

                Log.d("BndCountService", "$newCount")
                viewModel.count.postValue(newCount)
            }
        }
    }

    fun stopCounting() {
        countingJob?.cancel()
    }

    inner class CountingBinder : Binder() {
        fun getService() : BoundCountingService = this@BoundCountingService
    }

    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }
}