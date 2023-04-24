package com.mavino.hlsstreamer.exoplayer

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

class Exoplayer (val context: Context){


    //setup cache sink
    val cacheSink = CacheDataSink.Factory()
        .setCache(ExoApplication.cache)

    //setup upstream factory (network streaming)
    val upStreamFactory = DefaultDataSource.Factory(
        context,
        DefaultHttpDataSource.Factory()
    )

    //setup downstream factory (local streaming)
    val downStreamFactory = FileDataSource.Factory()

    //setup cacheDataSourceFactory
    val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(ExoApplication.cache)
        .setCacheWriteDataSinkFactory(cacheSink)
        .setUpstreamDataSourceFactory(upStreamFactory)
        .setCacheReadDataSourceFactory(downStreamFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
       // .setEventListener(cacheEventListner())

    val mediaItem = MediaItem.fromUri("")

    val mediaSource = HlsMediaSource.Factory(cacheDataSourceFactory)
        .createMediaSource(mediaItem)

    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    init {
        player.setMediaSource(mediaSource)
    }

}