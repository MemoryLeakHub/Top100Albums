package com.vrashkov.domain.paging.filter

data class AlbumsFilterQuery (
    var per_page: Int = 10,
    var page: Int = 0
)