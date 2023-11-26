package com.example.share_money_now

import FirebaseManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.share_money_now.login_screen.SigninScreen
import com.example.share_money_now.signup_screen.SignupScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Check if the user is already signed in
    val isUserSignedIn by remember { mutableStateOf(checkUserSignedIn()) }

    NavHost(navController = navController, startDestination = getStartDestination(isUserSignedIn)) {
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

private fun checkUserSignedIn(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
}

private fun getStartDestination(isUserSignedIn: Boolean): String {
    return if (isUserSignedIn) {
        Screen.LandingScreen.route
    } else {
        Screen.WelcomeScreen.route
    }
}