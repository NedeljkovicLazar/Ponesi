package com.lazar.ponesi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lazar.ponesi.navigation.AppNavigation
import com.lazar.ponesi.ui.theme.PonesiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PonesiTheme {
                AppNavigation()
            }
        }
    }
}