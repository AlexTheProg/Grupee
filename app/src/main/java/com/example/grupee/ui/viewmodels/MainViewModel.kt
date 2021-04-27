package com.example.grupee.ui.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.example.grupee.other.Constants
import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.example.grupee.other.Constants.MEDIA_ROOT_ID
import com.example.grupee.other.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

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
    private var liked: Boolean = false
    var likedSongsList: MutableList<String>? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private val mFirebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()



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

    @SuppressLint("LogNotTimber")
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
        liked = if(!liked){
            addToLikedSongsCollection(mediaItem)
            /*Log.d(TAG, "$mediaItem")*/
            likedSongsList?.add(mediaItem.mediaId)
            view.setImageResource(R.drawable.ic_favorite_liked)
            true
        }else{
            removeLikedSongsFromCollection(mediaItem)
            view.setImageResource(R.drawable.ic_favorite_unliked)
            likedSongsList?.remove(mediaItem.mediaId)
            /*Log.d(TAG, "S-a sters elementul ${mediaItem.mediaId}")*/
            false
        }
    }


    @SuppressLint("LogNotTimber")
   private fun addToLikedSongsCollection(mediaItem: Song){
        mFirebaseFirestore.collection(LIKED_SONGS_COLLECTION)
                .document(mediaItem.mediaId)
                .set(mediaItem)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Document Snapshot added with ID: ${mediaItem.mediaId}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding the document to the collection", e)
                }
    }

    @SuppressLint("LogNotTimber")
    private fun removeLikedSongsFromCollection(mediaItem: Song){
        mFirebaseFirestore.collection(LIKED_SONGS_COLLECTION)
                .document(mediaItem.mediaId)
                .delete()
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener {
                    e -> Log.w(ContentValues.TAG, "Error deleting document ${mediaItem.title}", e)
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

















