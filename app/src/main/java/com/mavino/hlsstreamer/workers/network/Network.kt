package com.mavino.hlsstreamer.workers.network

interface Network {
    fun fetchUrls(take: Int)
}