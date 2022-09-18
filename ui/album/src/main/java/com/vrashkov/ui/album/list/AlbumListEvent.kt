package com.vrashkov.ui.album.list

import com.vrashkov.core.base.BaseViewEvent

sealed class AlbumListEvent : BaseViewEvent(){
    data class OnAlbumClick(val id: String): AlbumListEvent()
    object SwipeRefresh: AlbumListEvent()
}