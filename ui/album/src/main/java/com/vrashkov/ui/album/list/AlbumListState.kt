package com.vrashkov.ui.album.list

import com.vrashkov.core.base.BaseViewState
import com.vrashkov.core.base.ProgressState

data class AlbumListState(
    val networkError: Boolean = false,
    val progress: ProgressState = ProgressState.Gone
): BaseViewState()
