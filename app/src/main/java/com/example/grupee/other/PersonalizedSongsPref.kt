package com.example.grupee.other

import android.content.Context
import androidx.core.content.edit

class PersonalizedSongsPref(appContext: Context) {

    companion object {
        const val KEY_LIKED_SONGS = "liked_songs"
        const val KEY_LIKED_SONGS_UPDATED = "liked_songs_updated"
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

    // Add single mediaId in liked songs and set liked songs updated to true.
    fun addLikedSong(mediaId: String) {
        var likedSongs = getLikedSongs()
        if (likedSongs == null) likedSongs = HashSet()

        likedSongs.add(mediaId)

        pref.edit(commit = true) {
            putStringSet(KEY_LIKED_SONGS, likedSongs)
            putBoolean(KEY_LIKED_SONGS_UPDATED, true)
        }
    }

    // Remove single mediaId from liked songs and set liked songs updated to true.
    fun removeLikedSong(mediaId: String) {
        getLikedSongs()?.apply {
            remove(mediaId)

            pref.edit(commit = true) {
                putStringSet(KEY_LIKED_SONGS, this@apply)
                putBoolean(KEY_LIKED_SONGS_UPDATED, true)
            }
        }
    }

    // Reset liked songs.
    fun resetLikedSongs() {
        pref.edit(commit = true) {
            putStringSet(KEY_LIKED_SONGS, hashSetOf())
        }
    }

    // Returns boolean indicates whether liked songs are updated.
    fun areLikedSongsUpdated() = pref.getBoolean(KEY_LIKED_SONGS_UPDATED, false)

    // Set liked songs updated to false.
    fun resetLikedSongsUpdated() {
        pref.edit(commit = true) {
            putBoolean(KEY_LIKED_SONGS_UPDATED, false)
        }
    }
}