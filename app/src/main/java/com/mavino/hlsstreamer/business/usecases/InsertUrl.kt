package com.mavino.hlsstreamer.business.usecases

import com.mavino.hlsstreamer.business.data.util.safeCacheCall
import com.mavino.hlsstreamer.business.data.cache.CacheResponseHandler
import com.mavino.hlsstreamer.business.data.cache.abstraction.VideoCacheDataSource
import com.mavino.hlsstreamer.business.domain.state.*
import com.mavino.hlsstreamer.framework.presentation.VideoViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertUrl(
    private val videoCacheDataSource: VideoCacheDataSource
){

    fun execute(
        url: String,
        stateEvent: StateEvent
    ): Flow<DataState<VideoViewState>?> = flow{

        val cacheResult = safeCacheCall(IO){
            videoCacheDataSource.insertUrl(url)
        }

        val response = object : CacheResponseHandler<VideoViewState, Long>(
            cacheResult,
            stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<VideoViewState>? {
                return if (resultObj > 0){
                    DataState.data(
                        response = Response(
                            message = INSERT_URL_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }else {
                    DataState.data(
                        response = Response(
                            message = INSERT_URL_FAILED,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }

        }.getResult()

        emit(response)
    }

    companion object{
        const val INSERT_URL_SUCCESS = "url succesfully inserted"
        const val INSERT_URL_FAILED = "insert url failed"
    }
}