package com.mavino.hlsstreamer.workers.video

interface VideoWork {

    fun downloadVideo(url: String)

    fun removeVideo(url: String)
}