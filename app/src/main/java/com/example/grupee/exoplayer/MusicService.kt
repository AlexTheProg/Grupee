package com.example.grupee.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.grupee.exoplayer.callbacks.MusicPlaybackPreparer
import com.example.grupee.exoplayer.callbacks.MusicPlayerEventListener
import com.example.grupee.exoplayer.callbacks.MusicPlayerNotificationListener
import com.example.grupee.other.Constants
import com.example.grupee.other.Constants.MEDIA_LIKED_SONGS_ID
import com.example.grupee.other.Constants.MEDIA_ROOT_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    @Inject
    lateinit var firebaseMusicSource: FirebaseMusicSource

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob) //+ coroutine scope are proprietatile dispatchers si serviceJob

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var curPlayingSong: MediaMetadataCompat? = null

    private var isPlayerInitialized = false

    private lateinit var musicPlayerEventListener: MusicPlayerEventListener

    private var parentId: String = MEDIA_ROOT_ID

    companion object {
        var curSongDuration = 0L
            private set
    }

    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        serviceScope.launch {
            firebaseMusicSource.fetchMediaData()
        }

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken

        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession.sessionToken,
            MusicPlayerNotificationListener(this)
        ) {
            curSongDuration = exoPlayer.duration
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession)

        setupMusicPlaybackPreparer()

        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicPlayerEventListener = MusicPlayerEventListener(this)
        exoPlayer.addListener(musicPlayerEventListener)
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun setupMusicPlaybackPreparer() {
        MusicPlaybackPreparer(firebaseMusicSource, parentId) {
            curPlayingSong = it

            val songs = when (parentId) {
                MEDIA_LIKED_SONGS_ID -> firebaseMusicSource.likedSongs
                else -> firebaseMusicSource.songs
            }

            preparePlayer(songs, it, playNow = true)

        }.let {
            mediaSessionConnector.setPlaybackPreparer(it)
        }
    }

    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            var songIndex = windowIndex

            return when (parentId) {
                MEDIA_LIKED_SONGS_ID -> {
                    if (songIndex > firebaseMusicSource.likedSongs.size - 1)
                        songIndex = 0

                    firebaseMusicSource.likedSongs[songIndex].description
                }
                else -> {
                    if (songIndex > firebaseMusicSource.songs.size - 1)
                        songIndex = 0

                    firebaseMusicSource.songs[songIndex].description
                }
            }
        }
    }

    private fun preparePlayer(
        songs: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        val curSongIndex = if (curPlayingSong == null) 0 else songs.indexOf(itemToPlay)

        serviceScope.launch {
            exoPlayer.prepare(firebaseMusicSource.asMediaSource(dataSourceFactory, parentId))
            exoPlayer.seekTo(curSongIndex, 0L)
            exoPlayer.playWhenReady = playNow
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()

        exoPlayer.removeListener(musicPlayerEventListener)
        exoPlayer.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): BrowserRoot? {
        return BrowserRoot(parentId, null)
    }

    @InternalCoroutinesApi
    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaBrowserCompat.MediaItem>>) {
        // Check whether liked songs updated.
        firebaseMusicSource.checkLikedSongs()

        this.parentId = parentId

        setupMusicPlaybackPreparer()

        when (parentId) {
            MEDIA_LIKED_SONGS_ID -> {
                val resultSent = firebaseMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(firebaseMusicSource.asMediaItems(parentId))
                        if (!isPlayerInitialized && firebaseMusicSource.likedSongs.isNotEmpty()) {
                            preparePlayer(firebaseMusicSource.likedSongs, firebaseMusicSource.likedSongs[0], playNow = false)
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultSent) {
                    result.detach()
                }
            }

            else -> { // MEDIA_ROOT_ID
                val resultSent = firebaseMusicSource.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(firebaseMusicSource.asMediaItems(parentId))
                        if (!isPlayerInitialized && firebaseMusicSource.songs.isNotEmpty()) {
                            preparePlayer(firebaseMusicSource.songs, firebaseMusicSource.songs[0], playNow = false)
                            isPlayerInitialized = true
                        }
                    } else {
                        result.sendResult(null)
                    }
                }
                if (!resultSent) {
                    result.detach()
                }
            }
        }
    }
}