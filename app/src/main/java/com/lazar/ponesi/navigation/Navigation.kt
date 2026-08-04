package com.lazar.ponesi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazar.ponesi.ui.screens.DocumentsScreen
import com.lazar.ponesi.ui.screens.HomeScreen


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Documents : Screen("documents")
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen()
        }

        composable(Screen.Documents.route) {
            DocumentsScreen()
        }
    }
}