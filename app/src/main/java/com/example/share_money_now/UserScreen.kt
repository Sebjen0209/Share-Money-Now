package com.example.share_money_now

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.Console

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(navController: NavController) {
    var userName by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.displayName) }
    var userEmail by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.email) }
    var newDebtUpdatesEnabled by remember { mutableStateOf(true) }
    var groupUpdatesEnabled by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }

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
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isEditing) {
                    OutlinedTextField(
                        value = userName ?: "",
                        onValueChange = { userName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = userEmail ?: "",
                        onValueChange = { userEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // Display current user information as Text components
                    Text(text = "Name: $userName", fontSize = 20.sp)
                    Text(text = "Email: $userEmail", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
        }

        // Button to toggle editing state
        Button(
            onClick = { isEditing = !isEditing },
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = if (isEditing) "Cancel" else "Edit Information",
                fontSize = 17.sp,
            )
        }

        // Button to update username and email
        if (isEditing) {
            Button(
                onClick = {
                    updateFirebaseUserInfo(userName ?: "", userEmail ?: "")
                    isEditing = false
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Update Information",
                    fontSize = 17.sp,
                )
            }
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

        // Button to Change Password
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Screen.WelcomeScreen.route)
            },
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Log Out",
                fontSize = 17.sp,
            )
        }
    }
}

// Function to update user information in Firebase
fun updateFirebaseUserInfo(newName: String, newEmail: String) {
    val user = FirebaseAuth.getInstance().currentUser
    val profileUpdater = UserProfileChangeRequest.Builder()
        .setDisplayName(newName)
        .build()

    user?.updateProfile(profileUpdater)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.updateEmail(newEmail)
                    .addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            println("User information updated successfully.")
                        } else {
                            println("Failed to update email.")
                        }
                    }
            } else {
                println("Failed to update user information.")
            }
        }
}