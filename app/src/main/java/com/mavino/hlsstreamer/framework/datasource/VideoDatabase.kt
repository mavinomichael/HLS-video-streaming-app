package com.mavino.hlsstreamer.framework.datasource

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VideoCacheEntity::class], version = 1)
abstract class VideoDatabase: RoomDatabase() {

    abstract fun videoDao(): VideoDao

    companion object{
        val DATABASE_NAME = "video_db"
    }
}