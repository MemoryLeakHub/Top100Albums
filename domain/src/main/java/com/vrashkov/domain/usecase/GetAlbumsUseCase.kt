package com.vrashkov.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vrashkov.core.base.DataState
import com.vrashkov.core.base.ProgressState
import com.vrashkov.domain.RequestResult
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.model.AlbumsResult
import com.vrashkov.domain.paging.AlbumsPaging
import com.vrashkov.domain.paging.filter.AlbumsFilterQuery
import com.vrashkov.domain.repository.AlbumsRepositoryLocal
import com.vrashkov.domain.repository.AlbumsRepositoryRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal,
    private val albumsRepositoryRemote: AlbumsRepositoryRemote
) {
    fun executeRemote(numberOfRecords: Int): Flow<DataState<AlbumsResult>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        when (val data = albumsRepositoryRemote.mostPlayedRecords(numberOfRecords = numberOfRecords)) {
            is RequestResult.Success -> {

                when(val deleteAllData = albumsRepositoryLocal.deleteAll()) {
                    is RequestResult.Error -> {
                        emit(DataState.Error(error = deleteAllData.exception))
                    }
                    is RequestResult.Success -> {
                        when (val insertAllData = albumsRepositoryLocal.insertAll(data.data.albumsList)) {
                            is RequestResult.Error -> {
                                emit(DataState.Error(error = insertAllData.exception))
                            }
                            is RequestResult.Success -> {
                            }
                        }
                    }
                }

                emit(DataState.Data(data = data.data))
            }
            is RequestResult.Error -> {
                emit(DataState.Error(error = data.exception))
            }
        }
    }

    fun execute(pageSize: Int): Flow<PagingData<AlbumSingle>> {

        val albums: Flow<PagingData<AlbumSingle>> = Pager(PagingConfig(pageSize = pageSize)) {
            AlbumsPaging(
                filter = AlbumsFilterQuery(per_page = pageSize),
                albumsRepositoryLocal = albumsRepositoryLocal
            )
        }.flow

        return albums

    }
}
