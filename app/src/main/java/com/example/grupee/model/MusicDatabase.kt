package com.example.grupee.model

import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.example.grupee.other.Constants.SONG_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.*
import kotlinx.coroutines.tasks.await


class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)
    private val likedSongColleciton = firestore.collection(LIKED_SONGS_COLLECTION)

    //suspendable -> coroutines, network call executed in co routine not in main thread
    suspend fun getAllSongs(): List<Song> {
        return try{
            songCollection.get().await().toObjects(Song::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    suspend fun getLikedSongs(): List<Song>{
        return try{
            likedSongColleciton.get().await().toObjects(Song::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

}