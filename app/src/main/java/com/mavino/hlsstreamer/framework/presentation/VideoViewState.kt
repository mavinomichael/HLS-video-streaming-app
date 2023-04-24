package com.mavino.hlsstreamer.framework.presentation

import com.mavino.hlsstreamer.business.domain.state.ViewState


data class VideoViewState(
    var insertMessage: String? = null
): ViewState