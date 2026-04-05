package com.example.midterm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midterm.ui.AdminScreen
import com.example.midterm.ui.AuthScreen
import com.example.midterm.viewmodel.ProductViewModel

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = if (viewModel.userState.value) "admin" else "auth") {
        composable("auth") {
            AuthScreen(viewModel, onNavigateToAdmin = {
                navController.navigate("admin") {
                    popUpTo("auth") { inclusive = true }
                }
            })
        }
        composable("admin") {
            AdminScreen(viewModel, onLogout = {
                navController.navigate("auth") {
                    popUpTo("admin") { inclusive = true }
                }
            })
        }
    }
}
