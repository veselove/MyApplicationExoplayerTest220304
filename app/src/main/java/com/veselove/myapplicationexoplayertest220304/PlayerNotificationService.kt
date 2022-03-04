package com.veselove.myapplicationexoplayertest220304

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

class PlayerNotificationService : Service() {

    companion object {
        const val PLAY_PAUSE_ACTION = "playPauseAction"
    }

    private lateinit var mPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private var notificationId = 123
    private var channelId = "channelId"

    override fun onCreate() {
        super.onCreate()
        val context = this
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Create a data source factory.
        dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, "app-name"))
        mPlayer.prepare(getListOfMediaSource())
        mPlayer.playWhenReady = true
        //setupEventListener()

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            channelId,
            R.string.channel_name,
            R.string.channel_desc,
            notificationId,
            object : PlayerNotificationManager.MediaDescriptionAdapter {

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    // return pending intent
                    val intent = Intent(context, MainActivity::class.java)
                    return PendingIntent.getActivity(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

                //pass description here
                override fun getCurrentContentText(player: Player): String {
                    return "Description"
                }

                //pass title (mostly playing audio name)
                override fun getCurrentContentTitle(player: Player): String {
                    return "Title"
                }

                // pass image as bitmap
                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    onGoing: Boolean) {

                    startForeground(notificationId, notification)

                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }

            }
        )
        //attach player to playerNotificationManager
        playerNotificationManager.setPlayer(mPlayer)
    }

    private fun setupEventListener() {

        val eventListener: Player.EventListener = object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                Log.d("zaq1", reason.toString())
                Log.d("zaq1", timeline.toString())
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                super.onPlaybackParametersChanged(playbackParameters)
                Log.d("zaq1", playbackParameters.toString())
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                Log.d("zaq1", "sdsdsd")
            }

            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
                super.onTimelineChanged(timeline, manifest, reason)
                Log.d("zaq1", "1")
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                super.onTracksChanged(trackGroups, trackSelections)
                Log.d("zaq1", "2")
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
                Log.d("zaq1", "3")
            }

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
                super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason)
                Log.d("zaq1", "4")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                Log.d("zaq1", "5")
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                Log.d("zaq1", "6")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                super.onShuffleModeEnabledChanged(shuffleModeEnabled)
                Log.d("zaq1", "7")
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                Log.d("zaq1", "8")
            }

            override fun onPositionDiscontinuity(reason: Int) {
                super.onPositionDiscontinuity(reason)
                Log.d("zaq1", "9")
            }

            override fun onSeekProcessed() {
                super.onSeekProcessed()
                Log.d("zaq1", "10")
            }

        }
        mPlayer.addListener(eventListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            val action = it.getIntExtra(PLAY_PAUSE_ACTION, -1)
            when (action) {
                0 -> mPlayer.playWhenReady = false
                1 -> mPlayer.next()
                2 -> mPlayer.previous()
                3 -> Log.d("zaq1", mPlayer.currentPosition.toString())
                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    // concatenatingMediaSource to pass media as a list,
    // so that we can easily prev, next
    private fun getListOfMediaSource(): ConcatenatingMediaSource {
        val mediaUrlList = ArrayList<String>()
        mediaUrlList.add("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        mediaUrlList.add("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
        mediaUrlList.add("http://d3rlna7iyyu8wu.cloudfront.net/skip_armstrong/skip_armstrong_stereo_subs.m3u8")
        mediaUrlList.add("https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8")
        mediaUrlList.add("https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8")

        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (mediaUrl in mediaUrlList) {
            concatenatingMediaSource.addMediaSource(buildMediaSource(mediaUrl))
        }

        return concatenatingMediaSource

    }

    //build media source to player
    private fun buildMediaSource(videoUrl: String): HlsMediaSource? {
        val uri = Uri.parse(videoUrl)
        // Create a HLS media source pointing to a playlist uri.
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    // detach player
    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        mPlayer.release()
        super.onDestroy()
    }

    //removing service when user swipe out our app
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

}