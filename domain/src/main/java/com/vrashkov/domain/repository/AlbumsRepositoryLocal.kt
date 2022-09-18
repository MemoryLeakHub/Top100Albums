package com.vrashkov.domain.repository

import com.vrashkov.datasource.database.dao.AlbumsDao
import com.vrashkov.datasource.database.entity.AlbumsEntity
import com.vrashkov.domain.RequestResult
import com.vrashkov.domain.map.AlbumSingleMapper
import com.vrashkov.domain.map.AlbumsEntityMapper
import com.vrashkov.domain.map.AlbumsMapper
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.model.AlbumsResult
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumsRepositoryLocal @Inject constructor(
    private val albumsDao: AlbumsDao
){

    suspend fun getAlbumsByPagingNotify(): Flow<RequestResult<AlbumsResult>> = flow {
        albumsDao.getAlbumsByPagingNotify().collect { data ->
            when (data) {
                is InitialResults -> {
                    if (data.list.size > 0) {
                        val result = AlbumsMapper.map(data.list)
                        emit(RequestResult.Success(result))
                    }
                }
                is UpdatedResults -> {
                    if (data.list.size > 0) {
                        val result = AlbumsMapper.map(data.list)
                        emit(RequestResult.Success(result))
                    }
                }
            }
        }
    }

    suspend fun getAlbumsByPaging(page: Int, limit: Int): RequestResult<List<AlbumSingle>> = withContext(Dispatchers.IO) {

        val albumList = albumsDao.getAlbumsByPaging(page = page, limit = limit).toList()

        val result = AlbumsMapper.map(albumList)
        if (result.albumsList.isNotEmpty()) {
            return@withContext RequestResult.Success(result.albumsList)
        } else {
            return@withContext RequestResult.Error(Exception("albums empty"))
        }

    }
    suspend fun getById(id: String): RequestResult<AlbumSingle> = withContext(Dispatchers.IO) {
        val album = albumsDao.getById(id = id)
        if (album != null)  {
            val result = AlbumSingleMapper.map(album)
            return@withContext RequestResult.Success(result)
        } else {
            return@withContext RequestResult.Error(Exception("album doesn't exist"))
        }
    }

    suspend fun insertAll(list: List<AlbumSingle>): RequestResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            val entityList = AlbumsEntityMapper.map(list)
            albumsDao.insertAll(entityList)
            return@withContext RequestResult.Success(true)
        } catch (e: Exception) {
            return@withContext RequestResult.Error(e)
        }
    }

    suspend fun deleteAll(): RequestResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            albumsDao.deleteAll()
            return@withContext RequestResult.Success(true)
        } catch (e: Exception) {
            return@withContext RequestResult.Error(e)
        }
    }
}