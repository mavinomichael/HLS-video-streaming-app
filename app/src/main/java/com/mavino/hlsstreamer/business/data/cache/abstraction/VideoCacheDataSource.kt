package com.mavino.hlsstreamer.business.data.cache.abstraction

import com.mavino.hlsstreamer.business.domain.model.Video

interface VideoCacheDataSource {

    suspend fun insertUrl(url: String): Long

    suspend fun getAllUrls(): List<Video>

    suspend fun deleteUrl(url: String): Int
}