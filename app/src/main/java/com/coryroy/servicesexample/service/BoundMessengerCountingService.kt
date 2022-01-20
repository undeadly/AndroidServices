package com.coryroy.servicesexample.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*

class BoundMessengerCountingService : Service(){

    private lateinit var messenger : Messenger

    override fun onBind(p0: Intent?): IBinder {
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

    internal class IncomingHandler(context: Context, private val applicationContext : Context = context.applicationContext) : Handler() {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_START -> startCounting()
                MSG_STOP -> stopCounting()

                else -> super.handleMessage(msg)

            }
        }

        fun startCounting() {
            countingJob = startCountingJob()
        }

        private fun startCountingJob() : Job {
            return CoroutineScope(Dispatchers.Default).launch {
                while (countingJob?.isActive != false) {
                    delay(1000)
                    val newCount = (CountingViewModel.count.value ?: 0) + 1

                    Log.d("BndMsgCountService", "$newCount")
                    CountingViewModel.count.postValue(newCount)
                }
            }
        }

        fun stopCounting() {
            countingJob?.cancel()
        }
    }





    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }

    companion object {
        const val MSG_START = 1
        const val MSG_STOP = 2
        var countingJob: Job? = null

    }
}