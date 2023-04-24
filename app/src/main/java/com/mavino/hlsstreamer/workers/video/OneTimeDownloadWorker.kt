package com.mavino.hlsstreamer.workers.video

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.IOException
import java.lang.Exception

class OneTimeDownloadWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val urls = listOf(
            "",
            ""
        )

        return try {
            urls.onEach { url ->
                VideoWorkImpl().downloadVideo(url)
            }

            Result.success()
        }catch (e: IOException){
            Result.failure()
        }catch (i: InterruptedException){
            Result.failure()
        }catch (e: Exception){
            Result.failure()
        }
    }


}