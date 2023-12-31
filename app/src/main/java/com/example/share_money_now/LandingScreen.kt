package com.example.share_money_now

import FirebaseManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.Person
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(navController: NavController, firebaseManager: FirebaseManager,
                  viewModel: CreateGroupViewModel = viewModel()) {
    var userName by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.displayName) }
    var newGroupName by remember { mutableStateOf("") }
    var isAddingGroup by remember { mutableStateOf(false) }
    val groups by viewModel.items.observeAsState(emptyList())
    var groupDescription by remember { mutableStateOf("") }

    val CreateGroupViewModel = viewModel<CreateGroupViewModel>()

    LaunchedEffect(Unit) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        currentUserEmail?.let { email ->
            CreateGroupViewModel.getItemsByMember(email)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.sharemoneynowlogo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(250.dp)
                .height(100.dp),

        )
        // User Name Button
        Button(
            onClick = {
                navController.navigate(Screen.UserScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
                Text(
                    text = "Welcome " + userName.toString() + "!",
                    color = Color.Black,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.height(50.dp)

                )
        }

        // Group List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            groups.forEach { item ->
                item {
                    var editableText by remember { mutableIntStateOf(100) }
                    val textColor =
                        if (editableText < 0) Color.Red else if (editableText > 0) Color(0xFF006400) else Color.Black
                    Button(
                        onClick = {
                            val groupId = item.id
                            navController.navigate("group_screen/$groupId")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name + "    -  ",
                                color = Color.Black,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(end = 8.dp),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "$editableText kr.",
                                color = textColor,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
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
            OutlinedTextField(
                value = groupDescription,
                onValueChange = { groupDescription = it },
                label = { Text("Group Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            // Button to Confirm New Group
            Button(
                onClick = {
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser != null) {
                        CreateGroupViewModel.fetchNameForEmail(currentUser.email ?: "") { associatedName ->
                            if (associatedName != null) {
                                val group = Group(
                                    UUID.randomUUID().toString(),
                                    currentUser.email ?: "",
                                    newGroupName,
                                    listOf(Person(currentUser.email ?: "", associatedName)),
                                    groupDescription,
                                    0.0,
                                    mapOf(((currentUser.email).toString()).replace(".","") to 0.0)
                                )

                                firebaseManager.createGroup(group)
                                if (newGroupName.isNotEmpty()) {
                                    newGroupName = ""
                                    isAddingGroup = false
                                    navController.navigate("group_screen/${group.id}")
                                } else {
                                    isButtonClicked = true
                                }
                            } else {
                                println("Associated name not found for the logged-in user.")
                            }
                        }
                    } else {
                        println("Current user is null.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Group")
            }
            Button(
                onClick = {
                    newGroupName = ""
                    groupDescription = ""
                    isAddingGroup = false
                    isButtonClicked = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFFFF5A5F ))
            ) {
                Text(text = "Cancel")
            }
        }
    }
}