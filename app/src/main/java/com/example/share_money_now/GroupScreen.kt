package com.example.share_money_now

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(navController: NavController) {
    var groupName by remember { mutableStateOf("Group Name") }
    var totalAmount by remember { mutableStateOf(1000.0) }
    var participants by remember { mutableStateOf(listOf("Participant 1", "Participant 2")) }
    var description by remember { mutableStateOf("Group Description") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = groupName) },
                actions = {
                    Button(onClick = { /* Handle edit button click */ }) {
                        Text(text = "Edit")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Total Amount
            Text(
                text = "Total Amount: $totalAmount",
            )

            // Participants
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(participants.size) { index ->
                    ParticipantRow(
                        name = participants[index],
                        debt = 50.0, // Replace with actual debt value
                        onRemoveClick = {
                            participants = participants.toMutableList().apply {
                                removeAt(index)
                            }
                        }
                    )
                }
            }

            // Description
            Text(
                text = "Description: $description",
            )

            // Edit Button
            Button(
                onClick = { /* Handle edit button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Edit")
            }

            // Add People Button
            Button(
                onClick = { /* Handle add people button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)
            ) {
                Text(text = "Add People")
            }

            // Pay Button
            Button(
                onClick = { /* Handle pay button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Pay My Part")
                }
            }
        }
    }
}

@Composable
fun ParticipantRow(name: String, debt: Double, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Participant Details
        Column {
            Text(text = name)
            Text(text = "Dept: $debt")
        }

        // Remove Button
        Button(onClick = onRemoveClick) {
            Text(text = "Remove")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGroupScreen() {
    // Preview code here
}
