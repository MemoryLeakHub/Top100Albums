package com.vrashkov.ui.album.list

import com.vrashkov.core.base.ActionsEvent
import com.vrashkov.core.base.NavigationEvent

sealed class AlbumListActionEvent: ActionsEvent() {
    object RefreshAlbumList: AlbumListActionEvent()
}