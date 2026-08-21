package com.lazar.ponesi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lazar.ponesi.PonesiApplication
import com.lazar.ponesi.ui.screens.CreateTravelScreen
import com.lazar.ponesi.ui.screens.DocumentsScreen
import com.lazar.ponesi.ui.screens.HistoryDetailsScreen
import com.lazar.ponesi.ui.screens.HistoryScreen
import com.lazar.ponesi.ui.screens.HomeScreen
import com.lazar.ponesi.ui.screens.TravelDetailsScreen
import com.lazar.ponesi.viewmodel.CreateTravelViewModel
import com.lazar.ponesi.viewmodel.DocumentsViewModel
import com.lazar.ponesi.viewmodel.HistoryDetailsViewModel
import com.lazar.ponesi.viewmodel.HistoryViewModel
import com.lazar.ponesi.viewmodel.HomeViewModel
import com.lazar.ponesi.viewmodel.TravelDetailsViewModel

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Documents : Screen("documents")

    object History : Screen("history")

    object HistoryDetails : Screen("history_details/{historyId}") {

        fun createRoute(historyId: Int): String {
            return "history_details/$historyId"
        }
    }

    object CreateTravel : Screen("create_travel")

    object TravelDetails : Screen("travel_details/{travelId}") {

        fun createRoute(travelId: Int): String {
            return "travel_details/$travelId"
        }
    }

    object EditTravel : Screen("edit_travel/{travelId}") {

        fun createRoute(travelId: Int): String {
            return "edit_travel/$travelId"
        }
    }
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
                    travelRepository = application.travelRepository,
                    weatherRepository = application.weatherRepository,
                    currentLocationProvider =
                        application.currentLocationProvider
                )
            }

            HomeScreen(
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onDocumentsClick = {
                    navController.navigate(Screen.Documents.route)
                },
                onCreateTravelClick = {
                    navController.navigate(Screen.CreateTravel.route)
                },
                onTravelClick = { travelId ->
                    navController.navigate(
                        Screen.TravelDetails.createRoute(travelId)
                    )
                },
                onEditTravelClick = { travelId ->
                    navController.navigate(
                        Screen.EditTravel.createRoute(travelId)
                    )
                },
                homeViewModel = homeViewModel
            )
        }

        composable(Screen.History.route) {

            val historyViewModel: HistoryViewModel = viewModel {
                HistoryViewModel(
                    travelHistoryRepository =
                        application.travelHistoryRepository
                )
            }

            HistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onHistoryClick = { historyId ->
                    navController.navigate(
                        Screen.HistoryDetails.createRoute(historyId)
                    )
                },
                historyViewModel = historyViewModel
            )
        }

        composable(
            route = Screen.HistoryDetails.route,
            arguments = listOf(
                navArgument("historyId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val historyId =
                backStackEntry.arguments?.getInt("historyId")
                    ?: return@composable

            val historyDetailsViewModel: HistoryDetailsViewModel =
                viewModel {
                    HistoryDetailsViewModel(
                        travelHistoryRepository =
                            application.travelHistoryRepository,
                        historyId = historyId
                    )
                }

            HistoryDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                historyDetailsViewModel = historyDetailsViewModel
            )
        }

        composable(Screen.Documents.route) {

            val documentsViewModel: DocumentsViewModel =
                viewModel {
                    DocumentsViewModel(
                        documentRepository =
                            application.documentRepository
                    )
                }

            DocumentsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                documentsViewModel = documentsViewModel
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

        composable(
            route = Screen.TravelDetails.route,
            arguments = listOf(
                navArgument("travelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val travelId =
                backStackEntry.arguments?.getInt("travelId")
                    ?: return@composable

            val travelDetailsViewModel: TravelDetailsViewModel =
                viewModel {
                    TravelDetailsViewModel(
                        travelRepository =
                            application.travelRepository,
                        weatherRepository =
                            application.weatherRepository,
                        travelId = travelId
                    )
                }

            TravelDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                travelDetailsViewModel = travelDetailsViewModel
            )
        }

        composable(
            route = Screen.EditTravel.route,
            arguments = listOf(
                navArgument("travelId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val travelId =
                backStackEntry.arguments?.getInt("travelId")
                    ?: return@composable

            val editTravelViewModel: CreateTravelViewModel = viewModel {
                CreateTravelViewModel(
                    travelRepository = application.travelRepository,
                    travelId = travelId
                )
            }

            CreateTravelScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                createTravelViewModel = editTravelViewModel
            )
        }
    }
}