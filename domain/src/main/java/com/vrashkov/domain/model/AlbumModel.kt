package com.vrashkov.domain.model

import java.time.LocalDate

data class AlbumsResult (
    val albumsList: MutableList<AlbumSingle> = mutableListOf(),
)
data class AlbumSingle (
    var index: Int = 1,
    var id: String = "",
    var albumUrl: String = "",
    var artistName: String = "",
    var albumName: String = "",
    var genres: List<String> = listOf(),
    var image: String = "",
    var releaseDate: LocalDate = LocalDate.now()
) {
    fun largeImage() : String {
        return this.image.replace("100x100bb", "1000x1000bb")
    }
}