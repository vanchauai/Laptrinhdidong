package com.example.midterm

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midterm.ui.ProductManagementScreen
import com.example.midterm.ui.UserAuthenticationScreen
import com.example.midterm.viewmodel.ProductViewModel

@Composable
fun AppRouter(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = if (viewModel.userState.value) "admin" else "auth") {
        composable("auth") {
            UserAuthenticationScreen(viewModel, onNavigateToAdmin = {
                navController.navigate("admin") {
                    popUpTo("auth") { inclusive = true }
                }
            })
        }
        composable("admin") {
            ProductManagementScreen(viewModel, onLogout = {
                navController.navigate("auth") {
                    popUpTo("admin") { inclusive = true }
                }
            })
        }
    }
}
