package com.example.kitabu_mad372_sf1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kitabu_mad372_sf1.ui.BookCatalogScreen
import com.example.kitabu_mad372_sf1.ui.LibraryViewModel
import com.example.kitabu_mad372_sf1.ui.UserDashboardScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: LibraryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "catalog"
    ) {
        composable("catalog") {
            BookCatalogScreen(
                viewModel = viewModel,
                onNavigateToDashboard = { navController.navigate("dashboard") }
            )
        }
        composable("dashboard") {
            UserDashboardScreen(
                viewModel = viewModel,
                onNavigateToCatalog = { navController.navigate("catalog") }
            )
        }
    }
}
