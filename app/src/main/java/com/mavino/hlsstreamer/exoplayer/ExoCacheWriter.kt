package com.mavino.hlsstreamer.exoplayer

import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheWriter
import com.mavino.hlsstreamer.utils.Constants.Companion.TAG
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExoCacheWriter(
    val cacheDataSource: CacheDataSource,
    val urls: List<String>
) {

    lateinit var currentUrl: String
    lateinit var videoUri: Uri

    lateinit var dataSpec: DataSpec

    private var cacheWriterListener: CacheWriterListener? = null

    @OptIn(DelicateCoroutinesApi::class)
    internal fun cacheVideos(){
        Log.d(TAG, "cscheVideos: caching is ongoing")

        urls.onEach {
            Log.d(TAG, "cscheVideos: current video $it")

            currentUrl = it
            videoUri = Uri.parse(currentUrl)
            dataSpec = DataSpec(videoUri, 0, 2000 * 1024, null)

            GlobalScope.launch (Dispatchers.IO){
                cacheVideo(dataSpec)
            }

        }
    }

    /**
     * requires
     * @param cachedatasource
     * @param dataspec
     * @param listener
     * */
    private fun cacheVideo(dataSpec: DataSpec){

        kotlin.runCatching {
            Log.d(TAG, "cscheVideo: in cache video")
            CacheWriter(
                cacheDataSource,
                dataSpec,
                null,
                cacheWriterListener()
            ).cache()
        }.onFailure {
            //failed to catch current video
            Log.d(TAG, "cacheVideoError: ${it.cause}")
        }

    }


    private fun cacheWriterListener(): CacheWriter.ProgressListener{
        Log.d(TAG, "cscheWriter: listener")
        return CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
            Log.d(TAG, "cscheWriter: $requestLength $bytesCached $newBytesCached")
            val progress: Double =  (bytesCached * 100.0 / requestLength)
            Log.d(TAG, "cacheWriterListener: $progress")
            cacheWriterListener?.progress(progress)
            /**
             * Expose listener
             * if its work request is carried out when user interacts with app
             * then we can switch to next url immediately when it gets to specific %
             */
        }
    }

    fun setOnCacheWriterListener(listener: CacheWriterListener){
        this.cacheWriterListener = listener
    }
}