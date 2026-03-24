package com.example.realtimefirebase

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

sealed class Screen (val rout: String) {
    object Home: Screen("home")
    object Signin: Screen("signin")
    object Signup: Screen("signup")
}

@Composable
fun Mynavigation()
{
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Signin.rout
    ){
        composable(Screen.Signin.rout){
            SignIn(navController = navController)
        }
        composable( Screen.Home.rout){
            HomeScreen(navController = navController)
        }
        composable(Screen.Signup.rout){
            SignUp(navController = navController)
        }
    }
}
