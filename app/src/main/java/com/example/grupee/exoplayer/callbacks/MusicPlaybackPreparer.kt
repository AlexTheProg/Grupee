package com.example.grupee.exoplayer.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.grupee.exoplayer.FirebaseMusicSource
import com.example.grupee.other.Constants
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import kotlinx.coroutines.InternalCoroutinesApi

class MusicPlaybackPreparer(
    private val firebaseMusicSource: FirebaseMusicSource,
    private val parentId: String = Constants.MEDIA_ROOT_ID,
    private val playerPrepared: (MediaMetadataCompat?) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    override fun onCommand(player: Player, controlDispatcher: ControlDispatcher, command: String, extras: Bundle?, cb: ResultReceiver?) = false


    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
            PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    @InternalCoroutinesApi
    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        firebaseMusicSource.whenReady {
            val itemToPlay = when (parentId) {
                Constants.MEDIA_LIKED_SONGS_ID ->
                    firebaseMusicSource.likedSongs.find { mediaId == it.description.mediaId }
                else -> firebaseMusicSource.songs.find { mediaId == it.description.mediaId }
            }

            playerPrepared(itemToPlay)
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
}