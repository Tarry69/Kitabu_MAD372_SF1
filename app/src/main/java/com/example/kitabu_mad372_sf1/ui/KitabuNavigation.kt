package com.example.kitabu_mad372_sf1.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Catalog : Screen("catalog", "Catalog", Icons.Default.Book)
    object Rentals : Screen("dashboard", "My Rentals", Icons.Default.Bookmark)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

@Composable
fun KitabuAppNavigation(
    navController: NavHostController,
    viewModel: LibraryViewModel
) {
    val items = listOf(
        Screen.Catalog,
        Screen.Rentals,
        Screen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    items.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    color = if (selected) Color(0xFF1B2269) else Color(0xFF757575)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1B2269),
                                selectedTextColor = Color(0xFF1B2269),
                                indicatorColor = Color(0xFFECEFFE),
                                unselectedIconColor = Color(0xFF757575),
                                unselectedTextColor = Color(0xFF757575)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Catalog.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Catalog.route) {
                BookCatalogScreen(
                    viewModel = viewModel,
                    onNavigateToDashboard = { navController.navigate(Screen.Rentals.route) }
                )
            }
            composable(Screen.Rentals.route) {
                UserDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToCatalog = { navController.navigate(Screen.Catalog.route) }
                )
            }
            composable(Screen.Profile.route) {
                UserProfileScreen()
            }
        }
    }
}
