package com.mavino.hlsstreamer.business.data.cache.implementation

import com.mavino.hlsstreamer.business.data.cache.abstraction.VideoCacheDataSource
import com.mavino.hlsstreamer.business.domain.model.Video
import com.mavino.hlsstreamer.framework.datasource.abstraction.VideoDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoCacheDataSourceImpl
@Inject
constructor(
    val videoDaoService: VideoDaoService
): VideoCacheDataSource {
    override suspend fun insertUrl(url: String): Long {
        return videoDaoService.insertUrl(url)
    }

    override suspend fun getAllUrls(): List<Video> {
        return videoDaoService.getAllUrls()
    }

    override suspend fun deleteUrl(url: String): Int {
        return videoDaoService.deleteUrl(url)
    }
}