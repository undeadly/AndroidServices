package com.coryroy.servicesexample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*

class BoundCountingService : Service(){

    private var countingJob: Job? = null

    inner class CountingBinder : Binder() {
        fun getService() : BoundCountingService = this@BoundCountingService
    }

    override fun onBind(intent: Intent?) = CountingBinder()

    fun startCounting() { countingJob = startCountingJob() }

    private fun startCountingJob() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (countingJob?.isActive != false) {
                delay(1000)
                val newCount = (CountingViewModel.count.value ?: 0) + 1

                Log.d("BndCountService", "$newCount")
                CountingViewModel.count.postValue(newCount)
            }
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopCounting()
        return super.onUnbind(intent)
    }

    fun stopCounting() { countingJob?.cancel() }

    override fun onDestroy() { countingJob?.cancel(); super.onDestroy() }

}
