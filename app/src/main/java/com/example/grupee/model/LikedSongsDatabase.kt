package com.example.grupee.model

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class LikedSongsDatabase {
    private val firestoreLikedSong = FirebaseFirestore.getInstance()
    private val likedSongCollection = firestoreLikedSong.collection(LIKED_SONGS_COLLECTION)

    suspend fun getAllLikedSongs(): List<Song>{
        return try{
            likedSongCollection.get().await().toObjects(Song::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    fun insertDataIntoCollection(song: LiveData<MediaMetadataCompat?>){
        likedSongCollection
                .add(song)
                .addOnSuccessListener { documentReference ->
                    Timber.log(1, "Document Snapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Timber.log(1, "Error adding the document", e)
                }
    }



}
