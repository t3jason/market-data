package com.doo.marketdata.model

data class MarketItem(
    val symbol: String,
    val currentPrice: Double,
    val priceChangePercent: Double
)
