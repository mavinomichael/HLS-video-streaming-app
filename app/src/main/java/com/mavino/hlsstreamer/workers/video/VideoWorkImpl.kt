package com.mavino.hlsstreamer.workers.video

import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.mavino.hlsstreamer.exoplayer.ExoApplication
import com.mavino.hlsstreamer.utils.Constants

class VideoWorkImpl: VideoWork {
    override fun downloadVideo(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        val downloadAction = HlsDownloader(
            mediaItem,
            ExoApplication.cacheDataSourceFactory
        )

        downloadAction.download { contentLength, bytesDownloaded, percentDownloaded ->
            Log.d(
                Constants.TAG, "doWork: content length: $contentLength  " +
                    "bytes downloaded: $bytesDownloaded  " +
                    "download percent: $percentDownloaded")
        }
    }

    override fun removeVideo(url: String) {
        ExoApplication.cache.removeResource(url)
    }
}