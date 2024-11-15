package com.doo.marketdata.data.api

import com.doo.marketdata.model.MarketItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MarketWebSocket {
    fun listenToMarketUpdates(initialData: List<MarketItem>): Flow<List<MarketItem>> = flow {
        var marketData = initialData
        while (true) {
            delay(10000)
            marketData = marketData.map { item ->
                item.copy(
                    currentPrice = item.currentPrice * (1 + Random.nextDouble(-0.05, 0.05)),
                    // TODO maybe add the calculations for percent change based on price change
                    priceChangePercent = Random.nextDouble(-5.0, 5.0)
                )
            }
            emit(marketData)
        }
    }
}
