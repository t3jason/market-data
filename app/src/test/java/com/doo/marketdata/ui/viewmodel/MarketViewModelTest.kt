package com.doo.marketdata.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.doo.marketdata.data.repository.MarketRepository
import com.doo.marketdata.model.MarketItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarketViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MarketViewModel
    private lateinit var repository: MarketRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()

        val fakePagingData = flowOf(PagingData.empty<MarketItem>())
        coEvery { repository.getPagedMarketData() } returns fakePagingData
        coEvery { repository.getMarketUpdates() } returns flowOf(  // Mocking this method too
            listOf(
                MarketItem("BTC/USD", 68000.0, 2.3),
                MarketItem("ETH/USD", 4800.0, 5.1)
            )
        )
        coEvery { repository.refreshMarketData() } returns listOf(
            MarketItem("BTC/USD", 68000.0, 2.3),
            MarketItem("ETH/USD", 4800.0, 5.1)
        )

        viewModel = MarketViewModel(repository)
    }

    @Test
    fun `refreshData updates isRefreshing and marketUpdates`() = runTest(testDispatcher) {
        viewModel.refreshData()

        assertTrue(viewModel.isRefreshing.value)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(
                MarketItem("BTC/USD", 68000.0, 2.3),
                MarketItem("ETH/USD", 4800.0, 5.1)
            ),
            viewModel.marketUpdates.value
        )

        assertFalse(viewModel.isRefreshing.value)
    }

    @After
    fun tearDown() {
        Dispatchers.setMain(Dispatchers.Default)
    }
}
