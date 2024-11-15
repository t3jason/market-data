package com.doo.marketdata.di
import com.doo.marketdata.data.api.MarketApiService
import com.doo.marketdata.data.api.MarketWebSocket
import com.doo.marketdata.data.repository.MarketRepository
import com.doo.marketdata.ui.viewmodel.MarketViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MarketApiService> { MarketApiService() }
    single<MarketWebSocket> { MarketWebSocket() }
    single<MarketRepository> { MarketRepository(get(), get()) }

    viewModel<MarketViewModel> { MarketViewModel(get()) }
}
