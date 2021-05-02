package com.example.grupee.model

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val artist: String = "",
    val songURL: String = "",
    val imageURL: String = ""
) {
    var liked: Boolean = false
}