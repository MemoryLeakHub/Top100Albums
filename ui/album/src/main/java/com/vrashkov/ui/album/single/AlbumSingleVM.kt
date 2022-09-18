package com.vrashkov.ui.album.single

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.vrashkov.core.base.BaseViewModel
import com.vrashkov.core.base.DataState
import com.vrashkov.core.base.NavigationEvent
import com.vrashkov.domain.usecase.GetAlbumSingleUseCase
import com.vrashkov.ui.album.list.AlbumListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumSingleVM
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAlbumSingleUseCase: GetAlbumSingleUseCase
): BaseViewModel<AlbumSingleState, AlbumSingleEvent>(){

    override val viewState: MutableState<AlbumSingleState> = mutableStateOf(AlbumSingleState())

    private val albumId: String? = savedStateHandle["id"]

    init {
        getAlbumSingle()
    }

    override fun onTriggerEvent(event: AlbumSingleEvent){

        when(event) {
            is AlbumSingleEvent.OnBackClick -> {
                viewModelScope.launch {
                    _navigationEventFlow.emit(NavigationEvent.NavigateBack)
                }
            }
        }
    }

    private fun getAlbumSingle() {
        viewModelScope.launch {
            if (!albumId.isNullOrEmpty()) {
                getAlbumSingleUseCase.execute(albumId).collect { result ->
                    when(result) {
                        is DataState.Data -> {
                            viewState.value = viewState.value.copy(
                                album = result.data
                            )
                        }
                        is DataState.Error -> {

                        }
                        is DataState.Loading -> {

                        }
                    }
                }
            } else { // if for some reason albumId is not found go to the albums
                _navigationEventFlow.emit(NavigationEvent.NavigateBack)
            }
        }
    }
}


