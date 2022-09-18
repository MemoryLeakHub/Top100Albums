package com.vrashkov.domain.map

import com.vrashkov.datasource.database.entity.AlbumsEntity
import com.vrashkov.datasource.network.response.MostPlayedRecordsResponse
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.model.AlbumsResult


class AlbumsMapper {
    companion object {
        fun map(entities: List<AlbumsEntity>): AlbumsResult {

            var albumsResult = AlbumsResult()

            entities.forEach {
                albumsResult.albumsList.add(
                    AlbumSingleMapper.map(it)
                )
            }

            return albumsResult
        }


        fun map(mostPlayedRecordsResponse: MostPlayedRecordsResponse): AlbumsResult {

            var albumsResult = AlbumsResult()

            mostPlayedRecordsResponse.feed.results.forEachIndexed { index, result ->
                albumsResult.albumsList.add(
                    AlbumSingleMapper.map(result, index)
                )
            }

            return albumsResult
        }
    }
}