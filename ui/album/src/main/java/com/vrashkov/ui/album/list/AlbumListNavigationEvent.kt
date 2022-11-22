package com.vrashkov.ui.album.list

import com.vrashkov.core.base.NavigationEvent

sealed class AlbumListNavigationEvent: NavigationEvent() {
    object NavigateToAlbumSingle: AlbumListNavigationEvent()
}