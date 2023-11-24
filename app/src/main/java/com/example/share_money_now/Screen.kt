package com.example.share_money_now

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen ("welcome_screen")
    object SigninScreen : Screen ("signin_screen")
    object SignupScreen : Screen ("signup_screen")
    object LandingScreen : Screen ("landing_screen")
    object UserScreen: Screen ("user_screen")
    object GroupScreen: Screen ("group_screen")
}
