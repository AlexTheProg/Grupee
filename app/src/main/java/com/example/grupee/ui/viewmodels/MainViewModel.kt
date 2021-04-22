package com.example.grupee.ui.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grupee.R
import com.example.grupee.exoplayer.*

import com.example.grupee.model.Song
import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.example.grupee.other.Constants.MEDIA_ROOT_ID
import com.example.grupee.other.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {
    private val _mediaItems = MutableLiveData<Resource<List<Song>>>()
    val mediaItems: LiveData<Resource<List<Song>>> = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState
    private var liked: Boolean = true


    private var mFirebaseAuth: FirebaseAuth? = null
    private val mFirebaseFirestore: FirebaseFirestore? = null
    private lateinit var likedSongsList: MutableList<MediaBrowserCompat.MediaItem>


    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()
                    )
                }
                _mediaItems.postValue(Resource.success(items))
            }
        })
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    @SuppressLint("LogNotTimber")
     fun likeSong(view: ImageButton, mediaItem: Song){
        liked = if(liked && mediaItem.mediaId == curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)){
            addToLikedSongsCollection(mediaItem)
            Log.d(TAG, "$mediaItem")
            view.setImageResource(R.drawable.ic_favorite_liked)
            false
        }else{
            view.setImageResource(R.drawable.ic_favorite_unliked)
            true
        }
    }

    private fun addToLikedSongsCollection(mediaItem: Song){
        mFirebaseFirestore?.collection(LIKED_SONGS_COLLECTION)
                    ?.add(mediaItem)
                    ?.addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Document Snapshot added with ID: ${documentReference.id}")
                    }
                    ?.addOnFailureListener { e ->
                        Log.w(TAG, "Error adding the document to the collection", e)
                    }
    }

     fun logout(view: View) {
        val user : FirebaseUser? = mFirebaseAuth?.currentUser
        if(user!=null) {
            mFirebaseAuth?.signOut()
        }
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if(isPrepared && mediaItem.mediaId ==
            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if(toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}

















