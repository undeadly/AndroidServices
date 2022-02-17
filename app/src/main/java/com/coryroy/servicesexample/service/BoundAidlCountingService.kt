package com.coryroy.servicesexample.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.coryroy.servicesexample.service.ICountingAidlInterface.Stub
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*

class BoundAidlCountingService : Service(){

    private var countingJob: Job? = null

    private val binder = object : ICountingAidlInterface.Stub() {
        override fun startCounting() { countingJob = startCountingJob() }
        override fun stopCounting() { countingJob?.cancel() }
    }

    override fun onBind(intent: Intent?): IBinder =  binder

    private fun startCountingJob() : Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (countingJob?.isActive != false) {
                delay(1000)
                val newCount = (CountingViewModel.count.value ?: 0) + 1
                Log.d("BndAIDLCountSVC", "$newCount")
                CountingViewModel.count.postValue(newCount)
            }
        }
    }

    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }
}