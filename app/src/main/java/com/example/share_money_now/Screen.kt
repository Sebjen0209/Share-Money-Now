package com.example.share_money_now

sealed class Screen(val route: String) {
    object WelcomePage : Screen ("welcome_screen")
    object SigninScreen : Screen ("signin_screen")
    object SignupScreen : Screen ("signup_screen")
}
