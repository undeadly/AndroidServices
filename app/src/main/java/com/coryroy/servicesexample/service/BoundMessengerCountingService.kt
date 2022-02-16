package com.coryroy.servicesexample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*

class BoundMessengerCountingService : Service(){

    companion object {
        const val MSG_START = 1
        const val MSG_STOP = 2
        var countingJob: Job? = null

        private fun startCounting() { countingJob = startCountingJob() }
        private fun stopCounting() { countingJob?.cancel() }
        private fun startCountingJob() : Job {
            return CoroutineScope(Dispatchers.Default).launch {
                while (countingJob?.isActive != false) {
                    delay(1000)
                    val newCount = (CountingViewModel.count.value ?: 0) + 1
                    CountingViewModel.count.postValue(newCount)
                } } } }
    
    override fun onBind(intent: Intent?): IBinder = Messenger(IncomingHandler(this)).binder

    internal class IncomingHandler(context: Context) : Handler(context.mainLooper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_START -> startCounting()
                MSG_STOP -> stopCounting()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onDestroy() { countingJob?.cancel(); super.onDestroy() }
}