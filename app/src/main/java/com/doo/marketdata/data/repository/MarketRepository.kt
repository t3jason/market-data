package com.doo.marketdata.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.doo.marketdata.data.api.MarketApiService
import com.doo.marketdata.data.api.MarketWebSocket
import com.doo.marketdata.model.MarketItem
import kotlinx.coroutines.flow.Flow

class MarketRepository(
    private val apiService: MarketApiService,
    private val webSocket: MarketWebSocket
) {
    fun getPagedMarketData(): Flow<PagingData<MarketItem>> {
        val initialData = apiService.fetchMarketData()
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { MarketPagingSource(initialData) }
        ).flow
    }

    fun getMarketUpdates(): Flow<List<MarketItem>> {
        return webSocket.listenToMarketUpdates(apiService.fetchMarketData())
    }

    fun refreshMarketData(): List<MarketItem> {
        return apiService.fetchMarketData()
    }
}
