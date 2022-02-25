package com.coryroy.servicesexample.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coryroy.servicesexample.work.CountingWorkManager.WORK_DATA_LABEL

class ShowNotificationWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private fun generateNotification() {
        WorkerNotification(context, inputData.getString(WORK_DATA_LABEL) ?: "Work!")
    }

    override suspend fun doWork(): Result {
        generateNotification()
        return Result.success()
    }

}
