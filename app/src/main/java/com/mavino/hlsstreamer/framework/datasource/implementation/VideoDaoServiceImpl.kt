package com.mavino.hlsstreamer.framework.datasource.implementation

import com.mavino.hlsstreamer.business.domain.model.Video
import com.mavino.hlsstreamer.framework.datasource.VideoDao
import com.mavino.hlsstreamer.framework.datasource.abstraction.VideoDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoDaoServiceImpl
@Inject
constructor(
    val videoDao: VideoDao
): VideoDaoService {
    override suspend fun insertUrl(url: String): Long {
        return videoDao.insertUrl(url)
    }

    override suspend fun getAllUrls(): List<Video> {
        return videoDao.getAllUrls()
    }

    override suspend fun deleteUrl(url: String): Int {
        return videoDao.deleteUrl(url)
    }
}