package com.doo.marketdata

import android.app.Application
import com.doo.marketdata.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MarketApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MarketApp)
            modules(appModule)
        }
    }
}
