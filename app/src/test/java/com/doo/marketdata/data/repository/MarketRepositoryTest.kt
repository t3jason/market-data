package com.doo.marketdata.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.doo.marketdata.data.api.MarketApiService
import com.doo.marketdata.data.api.MarketWebSocket
import com.doo.marketdata.model.MarketItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarketRepositoryTest {

    private lateinit var repository: MarketRepository
    private val apiService: MarketApiService = mockk()
    private val webSocket: MarketWebSocket = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val mockedData = listOf(
        MarketItem("BTC/USD", 68000.0, 2.3),
        MarketItem("ETH/USD", 4800.0, 5.1)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MarketRepository(apiService, webSocket)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPagedMarketData returns expected data snapshot`() = runTest(testDispatcher) {
        every { apiService.fetchMarketData() } returns mockedData

        val pagingDataFlow: Flow<PagingData<MarketItem>> = repository.getPagedMarketData()
        val snapshot = pagingDataFlow.asSnapshot()

        assertEquals(mockedData, snapshot)
    }

    @Test
    fun `getMarketUpdates returns expected updates`() = runTest(testDispatcher) {
        every { apiService.fetchMarketData() } returns mockedData
        coEvery { webSocket.listenToMarketUpdates(mockedData) } returns flowOf(mockedData)

        val updatesFlow = repository.getMarketUpdates()
        val updates = mutableListOf<List<MarketItem>>()
        updatesFlow.collect { updates.add(it) }

        assertEquals(1, updates.size)
        assertEquals(mockedData, updates.first())
    }


    @Test
    fun `refreshMarketData returns refreshed data`() = runTest(testDispatcher) {
        every { apiService.fetchMarketData() } returns mockedData

        val result = repository.refreshMarketData()

        assertEquals(mockedData, result)
    }
}
