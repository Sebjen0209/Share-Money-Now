package com.example.share_money_now

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), //temp logo
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        // Welcome Text
        Text(
            text = "Welcome to Share-Money-Now",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            textAlign = TextAlign.Center
        )

        // Sign In Button
        Button(
            onClick = {
                navController.navigate(Screen.SigninScreen.route)
            },
            modifier = Modifier
                .width(200.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Sign in",
                fontSize = 20.sp
            )
        }

        // Sign Up Button
        Button(
            onClick = {
                navController.navigate(Screen.SignupScreen.route)
            },
            modifier = Modifier.width(200.dp)
        ) {
            Text(
                text = "Sign up",
                fontSize = 20.sp
            )
        }
    }
}