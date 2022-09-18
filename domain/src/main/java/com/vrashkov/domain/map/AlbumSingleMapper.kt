package com.vrashkov.domain.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import com.vrashkov.datasource.database.entity.AlbumsEntity
import com.vrashkov.datasource.network.response.MostPlayedRecordsResponse
import com.vrashkov.domain.model.AlbumSingle
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*



class AlbumSingleMapper {
    companion object {
        fun map(entity: AlbumsEntity): AlbumSingle {

            val releasedDate: LocalDate = LocalDate.parse(entity.releaseDate)

            return AlbumSingle(
                index = entity.index,
                id = entity.id,
                albumUrl = entity.albumUrl,
                artistName = entity.artistName,
                albumName = entity.albumName,
                genres = entity.genres.split(","),
                image = entity.image,
                releaseDate = releasedDate
            )
        }


        fun map(entity: MostPlayedRecordsResponse.Feed.Result, index: Int): AlbumSingle {

            val releasedDate: LocalDate = LocalDate.parse(entity.releasedDate)

            return AlbumSingle(
                index = index,
                id = entity.id,
                albumUrl = entity.url,
                artistName = entity.artistName,
                albumName = entity.name,
                genres = entity.genres.map { it.name },
                image = entity.artworkUrl100,
                releaseDate = releasedDate
            )
        }
    }
}