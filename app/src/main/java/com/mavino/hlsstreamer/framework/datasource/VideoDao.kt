package com.mavino.hlsstreamer.framework.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mavino.hlsstreamer.business.domain.model.Video

@Dao
interface VideoDao {

    //insert url
    @Insert
    suspend fun insertUrl(url: String): Long

    //query all urls
    @Query("SELECT * FROM VIDEO_URLS")
    suspend fun getAllUrls(): List<Video>

    //delete url by url
    @Query("DELETE FROM VIDEO_URLS WHERE url = :url")
    suspend fun deleteUrl(url: String): Int
}