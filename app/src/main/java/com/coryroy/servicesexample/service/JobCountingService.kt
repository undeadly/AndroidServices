package com.coryroy.servicesexample.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.util.Log
import com.coryroy.servicesexample.viewmodel.CountingViewModel
import kotlinx.coroutines.*

class JobCountingService : JobService() {

    private val jobId = 77
    private var jobParams : JobParameters? = null
    private var countingJob: Job? = null
    var runningJob: Int? = null

    fun startJob(context: Context) {
        val serviceComponent = ComponentName(context, JobCountingService::class.java)

        val builder = JobInfo.Builder(jobId, serviceComponent)
        builder.setMinimumLatency(10)
        val jobScheduler =
            context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        runningJob = jobScheduler.schedule(builder.build())
    }

    fun stopJob(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        Log.d("JobCountService", "Calling jobFinished")
        jobScheduler.cancel(jobId)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        countingJob = startCountingJob()
        jobParams = params
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        countingJob?.cancel()
        return false
    }

    private fun startCountingJob(): Job {
        return CoroutineScope(Dispatchers.Default).launch {
            while (countingJob?.isCancelled != true) {
                delay(1000)
                val newCount = (CountingViewModel.count.value ?: 0) + 1
                CountingViewModel.count.postValue(newCount)
            }
            jobParams?.let {
                jobFinished(it, false)
            }
        }
    }

    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }
}