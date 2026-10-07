package com.example.kitabu_mad372_sf1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.kitabu_mad372_sf1.data.AppDatabase
import com.example.kitabu_mad372_sf1.repository.LibraryRepository
import com.example.kitabu_mad372_sf1.ui.KitabuAppNavigation
import com.example.kitabu_mad372_sf1.ui.LibraryViewModel
import com.example.kitabu_mad372_sf1.ui.theme.Kitabu_MAD372_SF1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = LibraryRepository(database.bookDao(), database.bookingDao())
        val viewModel: LibraryViewModel by viewModels {
            LibraryViewModel.Factory(repository)
        }

        enableEdgeToEdge()
        setContent {
            Kitabu_MAD372_SF1Theme {
                val navController = rememberNavController()
                KitabuAppNavigation(navController = navController, viewModel = viewModel)
            }
        }
    }
}
