package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppThemeMode
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PortfolioViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Support edge-to-edge full screen drawing
        enableEdgeToEdge()

        setContent {
            // Instantiate the central portfolio state machine
            val viewModel: PortfolioViewModel = viewModel()
            
            // Collect theme configurations
            val activeThemeMode by viewModel.themeMode.collectAsState()
            val activeAccentColor by viewModel.accentColor.collectAsState()

            // Resolve System Dark vs Custom selection
            val systemDark = isSystemInDarkTheme()
            val resolveDarkTheme = when (activeThemeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> systemDark
            }

            // Real-time toast feedback listener
            LaunchedEffect(viewModel.feedbackChannel) {
                viewModel.feedbackChannel.collectLatest { msg ->
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }

            MyApplicationTheme(
                darkTheme = resolveDarkTheme,
                accentColor = activeAccentColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}
