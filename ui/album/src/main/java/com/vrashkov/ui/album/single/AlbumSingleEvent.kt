package com.vrashkov.ui.album.single

import com.vrashkov.core.base.BaseViewEvent

sealed class AlbumSingleEvent : BaseViewEvent(){
    object OnBackClick: AlbumSingleEvent()
}