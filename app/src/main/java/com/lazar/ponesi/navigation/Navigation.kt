package com.lazar.ponesi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazar.ponesi.PonesiApplication
import com.lazar.ponesi.ui.screens.CreateTravelScreen
import com.lazar.ponesi.ui.screens.DocumentsScreen
import com.lazar.ponesi.ui.screens.HomeScreen
import com.lazar.ponesi.viewmodel.CreateTravelViewModel
import com.lazar.ponesi.viewmodel.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Documents : Screen("documents")
    object CreateTravel : Screen("create_travel")
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val application =
        LocalContext.current.applicationContext as PonesiApplication

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {

            val homeViewModel: HomeViewModel = viewModel {
                HomeViewModel(
                    travelRepository = application.travelRepository
                )
            }

            HomeScreen(
                onDocumentsClick = {
                    navController.navigate(Screen.Documents.route)
                },
                onCreateTravelClick = {
                    navController.navigate(Screen.CreateTravel.route)
                },
                homeViewModel = homeViewModel
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

            val createTravelViewModel: CreateTravelViewModel = viewModel {
                CreateTravelViewModel(
                    travelRepository = application.travelRepository
                )
            }

            CreateTravelScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                createTravelViewModel = createTravelViewModel
            )
        }
    }
}