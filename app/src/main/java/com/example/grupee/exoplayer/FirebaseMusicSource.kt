package com.example.grupee.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.example.grupee.model.MusicDatabase
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
        private val musicDatabase: MusicDatabase
){

    var songs = emptyList<MediaMetadataCompat>()
    var likedSongs = emptyList<MediaMetadataCompat>()

    @InternalCoroutinesApi
    suspend fun fetchMediaData() = withContext(Dispatchers.IO){
        state = State.STATE_INITIALIZING
        val allSongs = musicDatabase.getAllSongs()
        val allLikedSongs = musicDatabase.getLikedSongs()
        songs = allSongs.map{song ->
            Builder()
                    .putString(METADATA_KEY_ARTIST, song.artist)
                    .putString(METADATA_KEY_MEDIA_ID, song.mediaId)
                    .putString(METADATA_KEY_TITLE, song.title)
                    .putString(METADATA_KEY_DISPLAY_TITLE, song.title)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, song.imageURL)
                    .putString(METADATA_KEY_MEDIA_URI, song.songURL)
                    .putString(METADATA_KEY_ALBUM_ART_URI, song.imageURL)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, song.artist)
                    .putString(METADATA_KEY_DISPLAY_DESCRIPTION, song.artist)
                    .build()
        }

        likedSongs = allLikedSongs.map { song ->
            Builder()
                    .putString(METADATA_KEY_ARTIST, song.artist)
                    .putString(METADATA_KEY_MEDIA_ID, song.mediaId)
                    .putString(METADATA_KEY_TITLE, song.title)
                    .putString(METADATA_KEY_DISPLAY_TITLE, song.title)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, song.imageURL)
                    .putString(METADATA_KEY_MEDIA_URI, song.songURL)
                    .putString(METADATA_KEY_ALBUM_ART_URI, song.imageURL)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, song.artist)
                    .putString(METADATA_KEY_DISPLAY_DESCRIPTION, song.artist)
                    .build()
        }

        state = State.STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource{
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach{ song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        likedSongs.forEach{song ->
            val mediaSourceLikedSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(song.getString(METADATA_KEY_MEDIA_URI).toUri())

        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map{ song ->
        val desc = MediaDescriptionCompat.Builder()
                .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
                .setTitle(song.description.title)
                .setSubtitle(song.description.subtitle)
                .setMediaId(song.description.mediaId)
                .setIconUri(song.description.iconUri)
                .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>() //list of lambda functions to tell us if the source was initialized

    @InternalCoroutinesApi
    private var state: State = State.STATE_CREATED
        set(value){
            if(value == State.STATE_INITIALIZED || value == State.STATE_ERROR){ //check if it is alredy initialized
                synchronized(onReadyListeners){
                    field = value
                    onReadyListeners.forEach{listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            }else{
                field = value
            }
        }


    @InternalCoroutinesApi
    fun whenReady(action: (Boolean) -> Unit) : Boolean{  //returneaza boolean
        if(state == State.STATE_CREATED || state == State.STATE_INITIALIZING){
            onReadyListeners += action
            return false
        }else{
            action(state == State.STATE_INITIALIZED)
            return true
        }
    }
}

enum class State{
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}