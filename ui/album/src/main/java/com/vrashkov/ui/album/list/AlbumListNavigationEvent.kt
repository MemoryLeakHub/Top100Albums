package com.vrashkov.ui.album.list

import com.vrashkov.core.base.NavigationEvent

sealed class AlbumListNavigationEvent: NavigationEvent() {
    data class NavigateToAlbumSingle(val id: String): AlbumListNavigationEvent()
}