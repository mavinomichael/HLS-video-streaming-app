package com.mavino.hlsstreamer.utils

data class DownloadException(
    val url: String,
    val exception: ArrayList<String>
)
