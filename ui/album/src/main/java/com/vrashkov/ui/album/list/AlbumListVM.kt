package com.vrashkov.ui.album.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vrashkov.core.base.BaseViewModel
import com.vrashkov.core.base.DataState
import com.vrashkov.core.base.ProgressState
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.usecase.GetAlbumsNotifyUseCase
import com.vrashkov.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListVM
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val albumsUseCase: GetAlbumsUseCase,
    private val getAlbumsNotifyUseCase: GetAlbumsNotifyUseCase
): BaseViewModel<AlbumListState, AlbumListEvent>(){

    override val viewState: MutableState<AlbumListState> = mutableStateOf(AlbumListState())

    lateinit var lazyAlbumsItems: Flow<PagingData<AlbumSingle>>

    init {
        LoadAlbumsRemote()
        ChangeAlbumsListData()
        NotifyOnAlbumsChange()
    }

    override fun onTriggerEvent(event: AlbumListEvent){
        when (event) {
            is AlbumListEvent.OnAlbumClick -> { viewModelScope.launch {
                _navigationEventFlow.emit(AlbumListNavigationEvent.NavigateToAlbumSingle(event.id))
                }
            }
            is AlbumListEvent.SwipeRefresh -> {  viewModelScope.launch {
                    LoadAlbumsRemote()
                }
            }
        }
    }

    private fun LoadAlbumsRemote() {
        albumsUseCase.executeRemote(numberOfRecords = 100).onEach { data ->
            when(data) {
                is DataState.Data -> {
                    viewState.value = viewState.value.copy(
                        networkError = false,
                        progress = ProgressState.Gone
                    )
                }
                is DataState.Error -> {
                    // if local database is empty and there is an issue with the remote request
                    // user can swipe to try again
                    viewState.value = viewState.value.copy(
                        networkError = true,
                        progress = ProgressState.Gone
                    )

                }
                is DataState.Loading -> {
                    // if local database is empty we show the circle progress
                    viewState.value =
                        viewState.value.copy(progress = ProgressState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun ChangeAlbumsListData() {
        lazyAlbumsItems = albumsUseCase.execute(pageSize = 10)
    }
    private fun NotifyOnAlbumsChange() {
        getAlbumsNotifyUseCase.execute().onEach { data ->
            when(data) {
                is DataState.Data -> {
                    _actionsEventFlow.emit(AlbumListActionEvent.RefreshAlbumList)
                }
                is DataState.Error -> {

                }
                is DataState.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}


