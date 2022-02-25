package com.coryroy.servicesexample.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.delay

class CountingWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context,parameters) {
    override suspend fun doWork(): Result {
        repeat(10) {
            delay(1000)
            val newCount = (CountingViewModel.count.value ?: 0) + 1
            Log.d("CountingWorker", "Count is now $newCount")
            CountingViewModel.count.postValue(newCount)
        }
        return Result.success()
    }
}
