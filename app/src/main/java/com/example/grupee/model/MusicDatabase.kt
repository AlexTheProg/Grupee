package com.example.grupee.model

import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.example.grupee.other.Constants.PERSONALIZED_SONGS_COLLECTIONS
import com.example.grupee.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)
    private val personalizedSongsCollection = firestore.collection(PERSONALIZED_SONGS_COLLECTIONS)

    //suspendable -> coroutines, network call executed in co routine not in main thread
    suspend fun getAllSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getLikedSongs(uid: String?): List<Song> {
        return try {
            when {
                uid != null -> {
                    personalizedSongsCollection.document(uid).collection(LIKED_SONGS_COLLECTION).get()
                        .await().toObjects(Song::class.java)
                }
                else -> {
                    emptyList()
                }
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

}