package com.doo.marketdata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.doo.marketdata.data.repository.MarketRepository
import com.doo.marketdata.model.MarketItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MarketViewModel(private val repository: MarketRepository) : ViewModel() {
    val marketData: Flow<PagingData<MarketItem>> =
        repository.getPagedMarketData().cachedIn(viewModelScope)

    private val _marketUpdates = MutableStateFlow<List<MarketItem>>(emptyList())
    val marketUpdates: StateFlow<List<MarketItem>> = _marketUpdates

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        repository.getMarketUpdates()
            .onEach { updates -> _marketUpdates.value = updates }
            .launchIn(viewModelScope)
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Simulate network call
            delay(1000)
            try {
                val refreshedData = repository.refreshMarketData()
                _marketUpdates.value = refreshedData
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
