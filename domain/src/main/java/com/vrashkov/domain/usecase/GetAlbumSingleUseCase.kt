package com.vrashkov.domain.usecase

import com.vrashkov.core.base.DataState
import com.vrashkov.core.base.ProgressState
import com.vrashkov.domain.RequestResult
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.repository.AlbumsRepositoryLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.GeneralSecurityException
import javax.inject.Inject

class GetAlbumSingleUseCase @Inject constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal
) {
    fun execute(id: String): Flow<DataState<AlbumSingle>> = flow {
        emit(DataState.Loading(progressState = ProgressState.Loading))

        when (val data = albumsRepositoryLocal.getById(id = id)) {
            is RequestResult.Success -> {
                emit(DataState.Data(data = data.data))
            }
            is RequestResult.Error -> {
                emit(DataState.Error(error = GeneralSecurityException()))
            }
        }
    }
}
