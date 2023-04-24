package com.mavino.hlsstreamer.framework.datasource.abstraction

import com.mavino.hlsstreamer.business.domain.model.Video

interface VideoDaoService {

    suspend fun insertUrl(url: String): Long

    suspend fun getAllUrls(): List<Video>

    suspend fun deleteUrl(url: String): Int
}