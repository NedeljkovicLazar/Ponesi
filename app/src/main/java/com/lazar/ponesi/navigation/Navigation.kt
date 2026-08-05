package com.lazar.ponesi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazar.ponesi.ui.screens.DocumentsScreen
import com.lazar.ponesi.ui.screens.HomeScreen
import com.lazar.ponesi.ui.screens.CreateTravelScreen


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Documents : Screen("documents")
    object CreateTravel : Screen("create_travel")
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {

            HomeScreen(

                onDocumentsClick = {
                    navController.navigate(Screen.Documents.route)
                },

                onCreateTravelClick = {
                    navController.navigate(Screen.CreateTravel.route)
                }

            )

        }

        composable(Screen.Documents.route) {

            DocumentsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )

        }

        composable(Screen.CreateTravel.route) {

            CreateTravelScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )

        }
    }
}