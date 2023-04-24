package com.mavino.hlsstreamer.utils

class Constants {

    companion object{
        const val CACHE_DIRECTORY = "downloads"
        const val PERIODIC_WORK_TAG = "edekeeperoid"
        // Name of Notification Channel for verbose notifications of background work
        @JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
            "Verbose WorkManager Notifications"
        const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever work starts"
        @JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
        const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
        const val NOTIFICATION_ID = 1

        const val PRE_VIDEO_CHAIN = "edekeeVideo"
        const val PERIODIC_WORK_INTERVAL: Long = 1L
        // Tag for logs
        const val TAG = "appDebug"
        const val DEBUG = true // enable logging
    }

}