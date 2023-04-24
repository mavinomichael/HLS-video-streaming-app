package com.mavino.hlsstreamer.framework.presentation

import com.mavino.hlsstreamer.business.domain.state.StateEvent

sealed class VideoStateEvents: StateEvent {

    class InsertUrlEvent(
        val url: String
    ): VideoStateEvents() {
        override fun errorInfo(): String {
            return "Error inserting url"
        }

        override fun eventName(): String {
            return "InsertUrlEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean = true
    }
}