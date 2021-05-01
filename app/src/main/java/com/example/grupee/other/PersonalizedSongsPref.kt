package com.example.grupee.other

import android.content.Context
import androidx.core.content.edit

class PersonalizedSongsPref(appContext: Context) {

    companion object {
        const val KEY_LIKED_SONGS = "liked_songs"
    }

    private val pref = appContext.getSharedPreferences("PersonalizedSongs", Context.MODE_PRIVATE)

    // Returns mediaIds of liked songs, or empty Set if no song is liked.
    fun getLikedSongs(): MutableSet<String>? = pref.getStringSet(KEY_LIKED_SONGS, hashSetOf())

    // Set mediaIds of liked songs.
    fun setLikedSongs(mediaIds: Set<String>) {
        pref.edit(commit = true) {
            putStringSet(KEY_LIKED_SONGS, mediaIds)
        }
    }

    // Add single mediaId in liked songs.
    fun addLikedSong(mediaId: String) {
        var likedSongs = getLikedSongs()
        if (likedSongs == null) likedSongs = HashSet<String>()

        likedSongs.add(mediaId)

        pref.edit(commit = true) {
            putStringSet(KEY_LIKED_SONGS, likedSongs)
        }
    }

    // Remove single mediaId from liked songs.
    fun removeLikedSong(mediaId: String) {
        getLikedSongs()?.apply {
            remove(mediaId)

            pref.edit(commit = true) {
                putStringSet(KEY_LIKED_SONGS, this@apply)
            }
        }
    }

    // Reset liked songs.
    fun resetLikedSongs() {
        pref.edit(commit = true) {
            putStringSet(KEY_LIKED_SONGS, hashSetOf())
        }
    }
}