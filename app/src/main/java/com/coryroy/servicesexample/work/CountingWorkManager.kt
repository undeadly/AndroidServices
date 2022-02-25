package com.coryroy.servicesexample.work

import android.content.Context
import androidx.work.*
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

object CountingWorkManager {

    const val COUNTER_WORK_TAG = "count"
    const val WORK_DATA_LABEL = "LABEL"
    const val WORK_DATA_RUNNING = "RUNNING"

    fun startCounting(context: Context, initialDelaySeconds : Long = 0) {

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        val showStartNotification = OneTimeWorkRequestBuilder<ShowNotificationWorker>()
            .addTag(COUNTER_WORK_TAG)
            .setConstraints(constraints)
            .setInputData(workDataOf(WORK_DATA_LABEL to "Working it in $initialDelaySeconds!", WORK_DATA_RUNNING to true ))
            .build()

        val count = OneTimeWorkRequestBuilder<CountingWorker>()
            .setInitialDelay(initialDelaySeconds, TimeUnit.SECONDS)
            .addTag(COUNTER_WORK_TAG)
            .build()

        val showEndNotification = OneTimeWorkRequestBuilder<ShowNotificationWorker>()
            .addTag(COUNTER_WORK_TAG)
            .setInputData(workDataOf(WORK_DATA_LABEL to "Worked it!", WORK_DATA_RUNNING to false ))
            .build()

        WorkManager.getInstance(context)
            .beginWith(showStartNotification)
            .then(count)
            .then(showEndNotification)
            .enqueue()
    }

    fun stopCounting(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(COUNTER_WORK_TAG)
        WorkerNotification(context, "Work cancelled!")
    }
}