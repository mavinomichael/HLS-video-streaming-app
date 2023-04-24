package com.mavino.hlsstreamer.di

import android.content.Context
import androidx.room.Room
import com.mavino.hlsstreamer.business.data.cache.abstraction.VideoCacheDataSource
import com.mavino.hlsstreamer.framework.datasource.abstraction.VideoDaoService
import com.mavino.hlsstreamer.business.data.cache.implementation.VideoCacheDataSourceImpl
import com.mavino.hlsstreamer.framework.datasource.implementation.VideoDaoServiceImpl
import com.mavino.hlsstreamer.business.usecases.InsertUrl
import com.mavino.hlsstreamer.business.usecases.UsecasesHandler
import com.mavino.hlsstreamer.framework.datasource.VideoDao
import com.mavino.hlsstreamer.framework.datasource.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VideoModule {

    @Singleton
    @Provides
    fun provideVideoDatabase(@ApplicationContext context: Context): VideoDatabase =
        Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            VideoDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideVideoDao(videoDatabase: VideoDatabase): VideoDao {
        return videoDatabase.videoDao()
    }

    @Singleton
    @Provides
    fun provideUsecasesHandler(videoCacheDataSource: VideoCacheDataSource): UsecasesHandler{
        return UsecasesHandler(InsertUrl(videoCacheDataSource))
    }

    @Singleton
    @Provides
    fun provideVideoDaoService(videoDao: VideoDao): VideoDaoService {
        return VideoDaoServiceImpl(videoDao)
    }

    @Singleton
    @Provides
    fun provideVideoCacheDataSource(videoDaoService: VideoDaoService): VideoCacheDataSource{
        return VideoCacheDataSourceImpl(videoDaoService)
    }
}