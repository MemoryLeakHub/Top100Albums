package com.vrashkov.domain.result

sealed class AlbumsNotifyResult {
    object RecordsInserted: AlbumsNotifyResult()
    object Error: AlbumsNotifyResult()
}