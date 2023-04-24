package com.mavino.hlsstreamer.workers.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UrlNetworkWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}