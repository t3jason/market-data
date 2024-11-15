package com.doo.marketdata.data.api

import com.doo.marketdata.data.mockedMarketData
import com.doo.marketdata.model.MarketItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MarketApiService() {

    fun fetchMarketData(): List<MarketItem> {
        val listType = object : TypeToken<List<MarketItem>>() {}.type
        return Gson().fromJson(mockedMarketData, listType)
    }
}
