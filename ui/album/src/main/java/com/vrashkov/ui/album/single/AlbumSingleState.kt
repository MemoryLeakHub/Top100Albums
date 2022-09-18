package com.vrashkov.ui.album.single

import com.vrashkov.core.base.BaseViewState
import com.vrashkov.domain.model.AlbumSingle

data class AlbumSingleState(
    val album: AlbumSingle? = null,
): BaseViewState()
