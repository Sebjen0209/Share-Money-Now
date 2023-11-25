package com.example.share_money_now

import FirebaseManager
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.share_money_now.login_screen.SigninScreen
import com.example.share_money_now.signup_screen.SignupScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
        composable(route = Screen.WelcomeScreen.route){
            WelcomeScreen(navController = navController)
        }

        composable(route = Screen.SigninScreen.route) {
            SigninScreen(navController = navController)
        }

        composable(route = Screen.SignupScreen.route) {
            SignupScreen(navController = navController)
        }

        composable(route = Screen.LandingScreen.route) {
            LandingScreen(navController = navController, firebaseManager = FirebaseManager())
        }

        composable(route = Screen.UserScreen.route) {
            UserScreen(navController = navController)
        }

        composable(route = Screen.GroupScreen.route) {
            GroupScreen(navController = navController)
        }
    }
}