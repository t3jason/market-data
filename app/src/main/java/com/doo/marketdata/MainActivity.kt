package com.doo.marketdata
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.doo.marketdata.ui.screens.MarketScreen
import com.doo.marketdata.ui.theme.MarketDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MarketDataTheme {
                MarketScreen()
            }
        }
    }
}

