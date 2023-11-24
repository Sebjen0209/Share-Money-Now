package com.example.share_money_now

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.WelcomePage.route) {
        composable(route = Screen.WelcomePage.route){
            WelcomePage(navController = navController)
        }

        composable(route = Screen.SigninScreen.route) {
            SigninScreen(navController = navController)
        }

        composable(route = Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }
    }
}