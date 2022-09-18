package com.vrashkov.domain.repository

import com.vrashkov.datasource.database.dao.AlbumsDao
import com.vrashkov.datasource.database.entity.AlbumsEntity
import com.vrashkov.datasource.network.ApiService
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumsRepositoryRemote @Inject constructor(
    private val apiService: ApiService
){
    suspend fun mostPlayedRecords(numberOfRecords: Int): RequestResult<AlbumsResult> = withContext(
        Dispatchers.IO) {
        try{
            val network = apiService.mostPlayedRecords(
                numberOfRecords = numberOfRecords
            )

            val networkResponse = network.body()!!

            val result = AlbumsMapper.map(networkResponse)
            return@withContext RequestResult.Success(result)

        } catch (e: Exception) {
            return@withContext (RequestResult.Error(e))
        }
    }
}