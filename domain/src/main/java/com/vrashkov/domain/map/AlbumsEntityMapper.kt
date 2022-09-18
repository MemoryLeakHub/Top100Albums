package com.vrashkov.domain.map

import com.vrashkov.datasource.database.entity.AlbumsEntity
import com.vrashkov.domain.model.AlbumSingle
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class AlbumsEntityMapper {
    companion object {
        fun map(entities: List<AlbumSingle>): List<AlbumsEntity> {


            val list: MutableList<AlbumsEntity> = mutableListOf()
            entities.forEach { data ->

                list.add(
                    AlbumsEntity().apply {
                        index = data.index
                        id = data.id
                        albumUrl = data.albumUrl
                        artistName = data.artistName
                        albumName = data.albumName
                        genres = data.genres.joinToString(",")
                        image = data.image
                        releaseDate = data.releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }
                )
            }

            return list
        }
    }
}