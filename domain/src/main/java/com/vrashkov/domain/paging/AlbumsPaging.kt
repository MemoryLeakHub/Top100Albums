package com.vrashkov.domain.paging

import androidx.compose.runtime.collectAsState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vrashkov.domain.RequestResult
import com.vrashkov.domain.model.AlbumSingle
import com.vrashkov.domain.model.AlbumsResult
import com.vrashkov.domain.paging.filter.AlbumsFilterQuery
import com.vrashkov.domain.repository.AlbumsRepositoryLocal
import kotlinx.coroutines.flow.first

class AlbumsPaging constructor(
    private val albumsRepositoryLocal: AlbumsRepositoryLocal,
    private val filter: AlbumsFilterQuery
) : PagingSource<Int, AlbumSingle>() {
    override fun getRefreshKey(state: PagingState<Int, AlbumSingle>): Int?
    {
        return null
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumSingle> {
        val nextPage = params.key ?: 1

        return try {
            filter.page = nextPage

            when (val result = albumsRepositoryLocal.getAlbumsByPaging(page = filter.page, limit = filter.per_page)) {
                is RequestResult.Error -> {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                is RequestResult.Success -> {
                    LoadResult.Page(
                        data = result.data,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (result.data.isEmpty()) null else nextPage + 1
                    )
                }
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
