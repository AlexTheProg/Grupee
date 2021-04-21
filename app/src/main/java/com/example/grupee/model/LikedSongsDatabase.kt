package com.example.grupee.model

import com.example.grupee.other.Constants.LIKED_SONGS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


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



}
