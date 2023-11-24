package com.example.share_money_now

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController){
    var userName by remember { mutableStateOf("John Doe") }
    var groups by remember { mutableStateOf(listOf("TEST")) }
    var newGroupName by remember { mutableStateOf("") }
    var isAddingGroup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        // User Name Button
        Button(
            onClick = {
                navController.navigate(Screen.UserScreen.route)
            },
            modifier = Modifier
                .width(200.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text(
                text = userName,
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center

            )
        }

        // Group List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(groups) { group ->
                var editableText by remember { mutableIntStateOf(0) }
                val textColor = if (editableText < 0) Color.Red else if (editableText > 0) Color.Green else Color.Black
                Button(
                    onClick = {
                        navController.navigate(Screen.GroupScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "$group    -    $editableText",
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Button to Add New Group
        if (!isAddingGroup) {
            Button(
                onClick = {
                    isAddingGroup = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add New Group")
            }
        }
        var isButtonClicked by remember { mutableStateOf(false) }

        // Display error message if newGroupName is empty and the button is clicked
        if (newGroupName.isEmpty() && isButtonClicked) {
            Text(
                text = "Please enter a group name",
                color = Color.Red,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Text Field to Enter New Group Name
        if (isAddingGroup) {
            OutlinedTextField(
                value = newGroupName,
                onValueChange = { newGroupName = it },
                label = { Text("New Group Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Button to Confirm New Group
            Button(
                onClick = {
                    if (newGroupName.isNotEmpty()) {
                        groups = groups + newGroupName
                        newGroupName = ""
                        isAddingGroup = false
                        navController.navigate(Screen.GroupScreen.route)
                    } else {
                        isButtonClicked = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Group")
            }
        }
    }
}