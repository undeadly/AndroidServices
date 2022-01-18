package com.coryroy.servicesexample

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*

class JobCountingService : JobService() {

    private val viewModel = CountingViewModel

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
                val newCount = (viewModel.count.value ?: 0) + 1

                Log.d("JobCountService", "$newCount")
                viewModel.count.postValue(newCount)
            }
            jobParams?.let {
                Log.d("JobCountService", "Calling jobFinished")
                jobFinished(it, false)
            }
        }
    }

    override fun onDestroy() {
        countingJob?.cancel()
        super.onDestroy()
    }

    companion object {
        private const val jobId = 77
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
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            Log.d("JobCountService", "Calling jobFinished")
            jobScheduler.cancel(jobId)
        }
    }

}