package com.doo.marketdata.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.doo.marketdata.model.MarketItem
import com.doo.marketdata.ui.viewmodel.MarketViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(viewModel: MarketViewModel = koinViewModel()) {
    val marketItems = viewModel.marketData.collectAsLazyPagingItems()
    val updates by viewModel.marketUpdates.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Market Data") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, "Trigger Refresh")
                    }
                }
            )
        },
        content = { paddingValues ->
            MarketList(
                paddingValues = paddingValues,
                marketItems = marketItems,
                updates = updates,
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refreshData() }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MarketList(
    paddingValues: PaddingValues,
    marketItems: LazyPagingItems<MarketItem>,
    updates: List<MarketItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val updatesMap = updates.associateBy { it.symbol }
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                MarketHeaderRow()
            }

            items(marketItems.itemCount) { index ->
                val marketItem = marketItems[index]
                marketItem?.let {
                    val updatedItem = updatesMap[it.symbol] ?: it
                    MarketItemRow(updatedItem, index)
                }
            }
        }
    }
}

@Composable
fun MarketHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Text(
            text = "Symbol",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Price",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Change (%)",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MarketItemRow(item: MarketItem, index: Int) {
    val backgroundColor = if (index % 2 == 0) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    val priceChangeColor = if (item.priceChangePercent < 0) Color.Red else Color(0xFF2E7D32)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = item.symbol,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = String.format(Locale.US, "%.2f", item.currentPrice),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = String.format(Locale.US, "%.2f%%", item.priceChangePercent),
            color = priceChangeColor,
            modifier = Modifier.weight(1f),
        )
    }
}
