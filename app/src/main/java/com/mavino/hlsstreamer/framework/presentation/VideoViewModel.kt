package com.mavino.hlsstreamer.framework.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mavino.hlsstreamer.business.data.cache.abstraction.VideoCacheDataSource
import com.mavino.hlsstreamer.business.domain.state.DataState
import com.mavino.hlsstreamer.business.domain.state.StateEvent
import com.mavino.hlsstreamer.business.usecases.InsertUrl
import com.mavino.hlsstreamer.business.usecases.UsecasesHandler
import com.mavino.hlsstreamer.framework.presentation.VideoStateEvents.InsertUrlEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class VideoViewModel
@Inject
constructor(
    application: Application,
    private val handler: UsecasesHandler
): BaseViewModel<VideoViewState>() {

    override fun handleNewData(data: VideoViewState) {
        data.let { viewState ->
            viewState.insertMessage?.let { response ->
                setInsertUrlResponse(response)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<VideoViewState>?> = when(stateEvent){

            is InsertUrlEvent -> {
                handler.insertUrl.execute(
                    url = stateEvent.url,
                   stateEvent =  stateEvent)
            }
            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }

    @ExperimentalCoroutinesApi
    override fun initNewViewState(): VideoViewState {
        return VideoViewState()
    }

    private fun setInsertUrlResponse(response: String){
        val update = getCurrentViewStateOrNew()
        update.insertMessage = response
        setViewState(update)

    }

}