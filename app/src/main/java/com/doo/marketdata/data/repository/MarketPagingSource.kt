package com.doo.marketdata.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.doo.marketdata.model.MarketItem

class MarketPagingSource(private val marketData: List<MarketItem>) : PagingSource<Int, MarketItem>() {
    override fun getRefreshKey(state: PagingState<Int, MarketItem>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarketItem> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        val start = (page - 1) * pageSize
        val end = kotlin.math.min(start + pageSize, marketData.size)

        val items = if (start < marketData.size) marketData.subList(start, end) else emptyList()
        return LoadResult.Page(
            data = items,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (end == marketData.size) null else page + 1
        )
    }
}
