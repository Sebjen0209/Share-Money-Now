package com.example.share_money_now

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun UserScreen(navController: NavController) {
    var userName by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.displayName) }
    var userEmail by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email) }
    var userPassword by remember { mutableStateOf("********") }
    var newDebtUpdatesEnabled by remember { mutableStateOf(true) }
    var groupUpdatesEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "User Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        // User Information
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side (Labels)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "Name:", fontSize = 20.sp)
                Text(text = "Email:", fontSize = 20.sp)
                Text(text = "Password:", fontSize = 20.sp)
            }

            // Spacer
            Spacer(modifier = Modifier.width(16.dp))

            // Right side (Values)
            Column (verticalArrangement = Arrangement.spacedBy(16.dp)) {
                userName?.let { Text(text = it, fontSize = 20.sp) }
                userEmail?.let { Text(text = it, fontSize = 20.sp) }
                Text(text = userPassword, fontSize = 20.sp)
            }
        }

        // Button to Change Password
        Button(
            onClick = {
                // Handle button click to change password
            },
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
        ) {
            Text(text = "Change Password")
        }


        // New Debt and Group Updates
        Text(
            text = "Notification Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "New Debt Updates", fontSize = 18.sp)
            Switch(
                checked = newDebtUpdatesEnabled,
                onCheckedChange = {
                    newDebtUpdatesEnabled = it
                    // Handle switch state change for New Debt Updates
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Group Updates", fontSize = 18.sp)
            Switch(
                checked = groupUpdatesEnabled,
                onCheckedChange = {
                    groupUpdatesEnabled = it
                    // Handle switch state change for Group Updates
                }
            )
        }
    }
}
