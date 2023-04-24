package com.mavino.hlsstreamer.exoplayer

import android.app.Application
import android.util.Log
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.*
import com.mavino.hlsstreamer.utils.Constants
import com.mavino.hlsstreamer.utils.Constants.Companion.TAG
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class ExoApplication: Application() {

    private val cacheSize: Long = 300 * 1024 * 1024

    private lateinit var evictor: LeastRecentlyUsedCacheEvictor

    private lateinit var dataBaseProvider: StandaloneDatabaseProvider

    private lateinit var cacheSink: CacheDataSink.Factory

    private lateinit var upStreamFactory: DefaultDataSource.Factory

    private lateinit var downStreamFactory: FileDataSource.Factory

    override fun onCreate() {
        super.onCreate()

        cacheDirectory = File(
            this.getExternalFilesDir(null),
            Constants.CACHE_DIRECTORY
        )

        evictor = LeastRecentlyUsedCacheEvictor(cacheSize)
        dataBaseProvider = StandaloneDatabaseProvider(this)

        cache = SimpleCache(
            cacheDirectory,
            NoOpCacheEvictor(),
            dataBaseProvider
        )

        //setup cache sink
        cacheSink = CacheDataSink.Factory()
            .setCache(cache)

        //setup upstream factory (network streaming)
        upStreamFactory = DefaultDataSource.Factory(
            this,
            DefaultHttpDataSource.Factory()
        )

        //setup downstream factory (local streaming)
        downStreamFactory = FileDataSource.Factory()

        //setup cacheDataSourceFactory
        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setCacheWriteDataSinkFactory(cacheSink)
            .setUpstreamDataSourceFactory(upStreamFactory)
            .setCacheReadDataSourceFactory(downStreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            .setEventListener(cacheEventListener())

        cacheDataSource = cacheDataSourceFactory.createDataSource()

    }


    companion object{
        lateinit var cache: SimpleCache

        lateinit var cacheDataSource: CacheDataSource

        lateinit var cacheDataSourceFactory: CacheDataSource.Factory

        lateinit var cacheDirectory: File

        fun cacheEventListener(): CacheDataSource.EventListener{
            return object : CacheDataSource.EventListener{
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                    Log.d(TAG, "onCachedBytesRead: from ExoApplication  $cacheSizeBytes  $cachedBytesRead")
                }

                override fun onCacheIgnored(reason: Int) {
                    Log.d(TAG, "onCachedBytesRead: from ExoApplication Error $reason")
                }
            }
        }

    }

}