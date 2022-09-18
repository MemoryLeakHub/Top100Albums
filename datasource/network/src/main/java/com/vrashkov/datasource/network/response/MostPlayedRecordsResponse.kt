package com.vrashkov.datasource.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class MostPlayedRecordsResponse(
    @Json(name="feed")
    val feed: Feed
) {
    data class Feed(
        @Json(name="results")
        val results: List<Result>
    ) {
        data class Result(
            @Json(name="artistName")
            val artistName: String,
            @Json(name="id")
            val id: String,
            @Json(name="name")
            val name: String,
            @Json(name="artworkUrl100")
            val artworkUrl100: String,
            @Json(name="genres")
            val genres: List<Genres>,
            @Json(name="releaseDate")
            val releasedDate: String,
            @Json(name="url")
            val url: String
        ) {
            data class Genres(
                @Json(name="name")
                val name: String
            )
        }
    }
}