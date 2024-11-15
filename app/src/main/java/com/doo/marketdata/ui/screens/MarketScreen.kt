package com.doo.marketdata.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.doo.marketdata.model.MarketItem
import com.doo.marketdata.ui.viewmodel.MarketViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(viewModel: MarketViewModel = koinViewModel()) {
    val marketItems = viewModel.marketData.collectAsLazyPagingItems()
    val updates by viewModel.marketUpdates.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Market Data") }) },
        content = { paddingValues ->
            MarketList(paddingValues, marketItems, updates)
        }
    )
}

@Composable
fun MarketList(paddingValues: PaddingValues, marketItems: LazyPagingItems<MarketItem>, updates: List<MarketItem>) {
    val updatesMap = updates.associateBy { it.symbol }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(marketItems.itemCount) { index ->
                val marketItem = marketItems[index]
                marketItem?.let {
                    val updatedItem = updatesMap[it.symbol] ?: it
                    MarketItemRow(updatedItem)
                }
            }
        }
    }
}

@Composable
fun MarketItemRow(item: MarketItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(item.symbol, modifier = Modifier.weight(1f))
        Text("${item.currentPrice}", modifier = Modifier.weight(1f))
        Text("${item.priceChangePercent}%", modifier = Modifier.weight(1f))
    }
}
