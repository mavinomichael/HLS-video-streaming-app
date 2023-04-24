package com.mavino.hlsstreamer.framework.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.mavino.hlsstreamer.R
import com.mavino.hlsstreamer.exoplayer.ExoApplication
import com.mavino.hlsstreamer.framework.presentation.VideoStateEvents.InsertUrlEvent
import com.mavino.hlsstreamer.utils.Constants
import com.mavino.hlsstreamer.utils.printLogD
import com.mavino.hlsstreamer.workers.network.UrlNetworkWorker
import com.mavino.hlsstreamer.workers.video.DownloadVideosWorker
import com.mavino.hlsstreamer.workers.video.OneTimeDownloadWorker
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var playerView: PlayerView? = null

    private var player: ExoPlayer? = null

    private var workManger: WorkManager? = null

    private var networkConstraints: Constraints? = null

    private lateinit var viewModel: VideoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[VideoViewModel::class.java]
        //test room
        viewModel.setStateEvent(
            InsertUrlEvent("https://edekee-m3u8.s3.us-east-2.amazonaws.com/001ClayPotter_1.m3u8")
        )

        subscribeObservers()


        workManger = WorkManager.getInstance(this)

        networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //setUpPeriodicWorkRequest()

        //setUpOneTimeRequest()

        setUpUniqueOneTimeRequest()

        playerView = findViewById(R.id.player_view)
        setUpPlayer()
    }

    private fun addMediaItem(mediaUrl: String) {
        //Creating a media item of HLS Type
        val mediaSource = HlsMediaSource.Factory(ExoApplication.cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))

        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
        player?.play()
    }

    private fun  setUpPlayer(){

        //initializing exoplayer
        player = ExoPlayer.Builder(this).build()

        //set up audio attributes
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
        player?.setAudioAttributes(audioAttributes, false)

        playerView?.player = player

        //hiding all the ui StyledPlayerView comes with
        playerView?.setShowNextButton(false)
        playerView?.setShowPreviousButton(false)

        //setting the scaling mode to scale to fit
        player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    }

    private fun setUpPeriodicWorkRequest(){
        val downloadUrlWorkRequest = PeriodicWorkRequestBuilder<DownloadVideosWorker>(
            Constants.PERIODIC_WORK_INTERVAL,
            TimeUnit.HOURS)
            .setConstraints(networkConstraints!!)
            .build()

        workManger!!.enqueueUniquePeriodicWork(
            Constants.PERIODIC_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            downloadUrlWorkRequest
        )
    }

    private fun setUpOneTimeRequest(){
        val urlRequest = OneTimeWorkRequest.Builder(
            UrlNetworkWorker::class.java)
            .setConstraints(networkConstraints!!)
            .build()

        val videoRequest = OneTimeWorkRequest.Builder(
            OneTimeDownloadWorker::class.java)
            .setConstraints(networkConstraints!!)
            .build()

        var chain = workManger!!.beginUniqueWork(
            Constants.PRE_VIDEO_CHAIN,
            ExistingWorkPolicy.REPLACE,
            urlRequest
        )

        chain = chain.then(videoRequest)

        chain.enqueue()
    }

    private fun setUpUniqueOneTimeRequest(){

        val videoRequest = OneTimeWorkRequest.Builder(
            DownloadVideosWorker::class.java)
            .setConstraints(networkConstraints!!)
            .build()

        workManger!!.enqueue(videoRequest)
    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(this) { videoViewState ->

            videoViewState.insertMessage?.let { message ->
                printLogD(this.toString(), message)
            }
        }
    }
}