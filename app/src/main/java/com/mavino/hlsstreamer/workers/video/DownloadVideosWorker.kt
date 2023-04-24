package com.mavino.hlsstreamer.workers.video

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mavino.hlsstreamer.utils.Constants.Companion.TAG
import com.mavino.hlsstreamer.utils.DownloadException
import java.io.IOException
import java.lang.Exception

class DownloadVideosWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        Log.d(TAG, "doWork: started download work")
        //this work requires network connectivity constraint
        //get urls from room
        val urls = listOf(
        "https://edekee-m3u8.s3.us-east-2.amazonaws.com/001ClayPotter_1.m3u8",
        "https://edekee-m3u8.s3.us-east-2.amazonaws.com/02112cbc-08d6-4cd3-8f17-e491fb55a132_1.m3u8",
        "https://edekee-m3u8.s3.us-east-2.amazonaws.com/069c44d7-1008-4dc2-9560-6132f84d9f10_1.m3u8"
       )

        /*
        * download a video
        * if sucessful add to array for successful downloads & room for downloaded videos
        * check for io and interrupted exception or any exception
        * retry download
        * if runattemptcount for that video is more than 3 move to next video
        * update array of failed download with video id
        * log both array size results
        * if failed download array is not empty output result.failure
        * if empty ouput success
        * */
        val successList: ArrayList<String> = ArrayList()
        //todo add the exception to failure list
        val failureList: ArrayList<DownloadException> = ArrayList()

        urls.onEach { url ->
            Log.d(TAG, "doWork: downloading this url: $url")
            val exceptions = ArrayList<String>()
            try {
                VideoWorkImpl().downloadVideo(url)

                //add to success array
                if (!successList.contains(url)){
                    successList.add(url)
                }

                //if this is a retry action remove url from failed list
                failureList.onEach { if (it.url == url ) failureList.remove(it) }
                Result.success()
            }catch (e: IOException){
                Log.d(TAG, "download video doWork IOException: ${e.message}")
                e.message?.let { exceptions.add("IOException: $it") }
                failureList.add(DownloadException(url, exceptions))
                Result.retry()
            }catch (i: InterruptedException){
                Log.d(TAG, "download video doWork InterruptedException: ${i.message}")
                i.message?.let { exceptions.add("InterruptedException: $it") }
                failureList.add(DownloadException(url, exceptions))
                Result.retry()
            }catch (e: Exception){
                Log.d(TAG, "download video doWork Exception: ${e.message}")
                e.message?.let { exceptions.add("Exception: $it") }
                failureList.add(DownloadException(url, exceptions))
                Result.retry()
            }
        }
        //todo update room tables
        Log.d(TAG, "doWork results: failureList: $failureList, successList: $successList")
        return if (failureList.isEmpty()) Result.success()
        else Result.failure()
    }
}