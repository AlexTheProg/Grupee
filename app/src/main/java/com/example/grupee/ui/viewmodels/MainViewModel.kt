package com.example.grupee.ui.viewmodels

import android.annotation.SuppressLint
import android.content.ContentValues
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grupee.R
import com.example.grupee.exoplayer.MusicServiceConnection
import com.example.grupee.exoplayer.isPlayEnabled
import com.example.grupee.exoplayer.isPlaying
import com.example.grupee.exoplayer.isPrepared
import com.example.grupee.model.Song
import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.example.grupee.other.Constants.MEDIA_ROOT_ID
import com.example.grupee.other.Constants.PERSONALIZED_SONGS_COLLECTIONS
import com.example.grupee.other.PersonalizedSongsPref
import com.example.grupee.other.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    @Inject
    lateinit var personalizedSongsPref: PersonalizedSongsPref

    private var mFirebaseAuth: FirebaseAuth? = null

    private val _mediaItems = MutableLiveData<Resource<List<Song>>>()
    val mediaItems: LiveData<Resource<List<Song>>> = _mediaItems

    private val _allSongsItems = MutableLiveData<Resource<List<Song>>>()
    val allSongsItems: LiveData<Resource<List<Song>>> = _allSongsItems

    private val _likedSongsItems = MutableLiveData<Resource<List<Song>>>()
    val likedSongsItems: LiveData<Resource<List<Song>>> = _likedSongsItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    var parentId: String = MEDIA_ROOT_ID

    var likedSongsList: MutableList<String>? = null
    private val mFirebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()

        subscribeMusicService(parentId)
    }

    fun subscribeMusicService(parentId: String = MEDIA_ROOT_ID) {
        this.parentId = parentId

        _mediaItems.postValue(Resource.loading(null))

        musicServiceConnection.subscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {
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
    fun likeSong(view: ImageButton, mediaItem: Song) {
        mFirebaseAuth?.currentUser?.let {
            when {
                !mediaItem.liked -> {
                    addToLikedSongsCollection(it.uid, view, mediaItem)
                    // likedSongsList?.add(mediaItem.mediaId)
                }
                else -> {
                    removeLikedSongsFromCollection(it.uid, view, mediaItem)
                    // likedSongsList?.remove(mediaItem.mediaId)
                }
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun addToLikedSongsCollection(uid: String, view: ImageButton, mediaItem: Song) {
        mFirebaseFirestore.collection(PERSONALIZED_SONGS_COLLECTIONS)
            .document(uid)
            .collection(LIKED_SONGS_COLLECTION)
            .document(mediaItem.mediaId)
            .set(mediaItem)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "Document Snapshot added with ID: ${mediaItem.mediaId}")

                view.setImageResource(R.drawable.ic_favorite_liked)

                // Add current song in PersonalizedSongsPref liked songs.
                personalizedSongsPref.addLikedSong(mediaItem.mediaId)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding the document to the collection", e)
            }
    }

    @SuppressLint("LogNotTimber")
    private fun removeLikedSongsFromCollection(uid: String, view: ImageButton, mediaItem: Song) {
        mFirebaseFirestore.collection(PERSONALIZED_SONGS_COLLECTIONS)
            .document(uid)
            .collection(LIKED_SONGS_COLLECTION)
            .document(mediaItem.mediaId)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")

                view.setImageResource(R.drawable.ic_favorite_unliked)

                // Remove current song from PersonalizedSongsPref liked songs.
                personalizedSongsPref.removeLikedSong(mediaItem.mediaId)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting document ${mediaItem.title}", e)
            }
    }

    fun logout(view: View) {
        // Reset liked songs in PersonalizedSongsPref.
        personalizedSongsPref.resetLikedSongs()

        val user: FirebaseUser? = mFirebaseAuth?.currentUser
        if (user != null) {
            mFirebaseAuth?.signOut()
        }
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        unsubscribeMusicService()

        super.onCleared()
    }

    fun unsubscribeMusicService() {
        if (playbackState.value?.isPlaying == true) musicServiceConnection.transportControls.pause()

        musicServiceConnection.unsubscribe(parentId, object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}