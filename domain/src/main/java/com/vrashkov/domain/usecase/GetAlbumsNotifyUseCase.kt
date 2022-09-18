package com.vrashkov.domain.usecase

import com.vrashkov.core.base.DataState
import com.vrashkov.core.base.ProgressState
import com.vrashkov.domain.RequestResult
import com.vrashkov.domain.repository.AlbumsRepositoryLocal
import com.vrashkov.domain.result.AlbumsNotifyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsNotifyUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal
) {
    fun execute(): Flow<DataState<AlbumsNotifyResult>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        albumsRepositoryLocal.getAlbumsByPagingNotify().collect { result ->
            when(result) {
                is RequestResult.Error -> {
                    emit(DataState.Data(AlbumsNotifyResult.Error))
                }
                is RequestResult.Success -> {
                    emit(DataState.Data(AlbumsNotifyResult.RecordsInserted))
                }
            }
        }

    }
}
