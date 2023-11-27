package com.example.share_money_now

import FirebaseManager
import PersonalGroupViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.share_money_now.data_classes.Group
import com.example.share_money_now.data_classes.Person
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.absoluteValue
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GroupScreen(navController: NavController,
                groupId: String?,
                firebaseManager: FirebaseManager,
                viewModel: PersonalGroupViewModel = viewModel()
) {

    var totalAmount by remember { mutableStateOf(0.0) }
    var participants by remember { mutableStateOf(listOf("")) }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDialogExpense by remember { mutableStateOf(false) }
    var showDialogPay by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf<Double?>(null) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var paidAmountMap: Map<String, Double> = emptyMap()
    var group by remember { mutableStateOf(Group()) }
    var cleanEmail = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "")
    var mail by remember { mutableStateOf("") }
    var paymentAmount by remember { mutableStateOf(0.0) }
    val gId = groupId.toString()
    val keyboardController = LocalSoftwareKeyboardController.current
    val personalGroupViewModel = viewModel<PersonalGroupViewModel>()
    val groupState by personalGroupViewModel.group.collectAsState()
    var debt by remember { mutableStateOf(0.0) }

    if (!groupId.isNullOrBlank()) {
        personalGroupViewModel.findGroupByGroupId(groupId) { firebaseGroupId ->
            if (firebaseGroupId != null) {
                personalGroupViewModel.fetchGroupDetails(firebaseGroupId) { fetchedGroup ->
                    if (fetchedGroup != null) {
                        group = fetchedGroup // Update the 'group' variable with fetched data

                        participants = fetchedGroup.members.map { it?.name ?: "" }

                        totalAmount = fetchedGroup.totalAmount

                        paidAmountMap = fetchedGroup.paidAmount

                        personalGroupViewModel.setGroup(fetchedGroup)

                    } else {
                        // Handle scenario when group data is null or not found
                    }
                }
            } else {
                // Handle scenario when groupId is not found
            }
        }
    }


    // LaunchedEffect for fetching group details
    LaunchedEffect(groupId) {
        if (!groupId.isNullOrBlank()) {
            personalGroupViewModel.findGroupByGroupId(groupId) { firebaseGroupId ->
                if (firebaseGroupId != null) {
                    personalGroupViewModel.fetchGroupDetails(firebaseGroupId) { fetchedGroup ->
                        if (fetchedGroup != null) {
                            group = fetchedGroup // Update the 'group' variable with fetched data

                            participants = fetchedGroup.members.map { it?.name ?: "" }

                            totalAmount = fetchedGroup.totalAmount

                            paidAmountMap = fetchedGroup.paidAmount

                            personalGroupViewModel.setGroup(fetchedGroup)

                        } else {
                            // Handle scenario when group data is null or not found
                        }
                    }
                } else {
                    // Handle scenario when groupId is not found
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = group.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            showEditDialog = true
                            keyboardController?.show()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_text),
                            contentDescription = stringResource(id = R.string.edit_button),
                            modifier = Modifier.size(24.dp)
                        )
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
                text = "Total Amount: ",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            Text(
                text = "$totalAmount",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )

            // Participants
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(participants.size) { index ->
                try {
                    if (group.members.size > 0){
                        mail = (group.members.get(index)?.email)?.replace(".","") ?: ""
                        debt = (totalAmount / participants.size) - (paidAmountMap[mail]?.toDouble() ?: 0.0)
                    }else{
                        debt = 0.0
                    }
                }
                catch (e: Exception){
                    debt = 0.0
                }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Participant Details
                        Column {
                            Text(text = participants.getOrNull(index) ?: "")
                            Text(text = "$debt" )
                        }

                        // Remove Button
                        Button(onClick = {
                            val emailToRemove = participants.getOrNull(index) ?: ""
                            val indexToRemove =
                                group.members.indexOfFirst { it?.name ?: "" == emailToRemove }

                            if (indexToRemove != -1) {
                                // Email is registered and found in the members list
                                // Remove the Person from the members list
                                val updatedMembers = group.members.toMutableList().apply {
                                    removeAt(indexToRemove)
                                }

                                // Update the group with the modified members list
                                group = group.copy(members = updatedMembers)

                                // Update the group in the Firebase Realtime Database
                                personalGroupViewModel.updateGroupInFirebase(group)

                                participants = group.members.map { it?.name ?: "" }
                            }
                        }

                        ) {
                            Text(text = "Remove")
                        }
                    }

                    /*
                    ParticipantRow(
                        name = participants.getOrNull(index) ?: "",
                        debt = ,
                        onRemoveClick = {
                            val emailToRemove = participants.getOrNull(index) ?: ""
                            val indexToRemove =
                                group.members.indexOfFirst { (it?.name ?: "") == emailToRemove }

                            if (indexToRemove != -1) {
                                // Email is registered and found in the members list
                                // Remove the Person from the members list
                                val updatedMembers = group.members.toMutableList().apply {
                                    removeAt(indexToRemove)
                                }

                                // Update the group with the modified members list
                                group = group.copy(members = updatedMembers)

                                // Update the group in the Firebase Realtime Database
                                personalGroupViewModel.updateGroupInFirebase(group)

                                participants = group.members.map { it?.name ?: "" }
                            }
                        }
                    )

                     */
                }
            }

            // Description
            Text(
                text = "Description: ${group.description}",
            )

            // Share group expenses Button
            Button(
                onClick = {
                    showDialogExpense = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Share Group Expense")
            }

            // Dialog for Share Group Expenses
            if (showDialogExpense) {
                Dialog(
                    onDismissRequest = {
                        showDialogExpense = false
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Enter Expense Amount",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = expenseValue.toString(),
                                onValueChange = {
                                    // Handle the case where the input is not a valid double
                                    expenseValue = it.toDoubleOrNull() ?: 0.0
                                },
                                label = { Text("Expense Amount") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        // Dismiss the dialog without processing the input
                                        showDialogExpense = false
                                    }
                                ) {
                                    Text(text = "Cancel")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                TextButton(
                                    onClick = {
                                        totalAmount += expenseValue!!
                                        group = group.copy(totalAmount = totalAmount)

                                            val updatedPaidAmount = group.paidAmount.toMutableMap().apply {
                                                val currentAmount = getOrDefault(cleanEmail, 0.0)
                                                if (cleanEmail != null) {
                                                    put(cleanEmail!!, currentAmount + expenseValue!!)
                                                }
                                            }
                                            group = group.copy(paidAmount = updatedPaidAmount)

                                            // Reset the paymentAmount and dismiss the dialog
                                            expenseValue = 0.0
                                            showDialogExpense = false

                                        personalGroupViewModel.updateGroupInFirebase(group)

                                        showDialogExpense = false
                                    }
                                ) {
                                    Text(text = "OK")
                                }
                            }
                        }
                    }
                )
            }

            // Pay Button
            Button(
                onClick = {
                    showDialogPay = true
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Pay My Part")
                }
            }

// ...

// Inside your composable function

            if (showDialogPay) {
                Dialog(
                    onDismissRequest = {
                        showDialogPay = false
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Enter Payment Amount",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = paymentAmount.toString(),
                                onValueChange = {
                                    // Handle the case where the input is not a valid double
                                    paymentAmount = it.toDoubleOrNull() ?: 0.0
                                },
                                label = { Text("Payment Amount") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        // Dismiss the dialog without processing the input
                                        showDialogPay = false
                                    }
                                ) {
                                    Text(text = "Cancel")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                TextButton(
                                    onClick = {
                                        // Update the paidAmount in the database with the entered paymentAmount
                                        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                                        if (currentUserEmail != null) {
                                            val cleanEmail = currentUserEmail.replace(".", "")
                                            val updatedPaidAmount = group.paidAmount.toMutableMap().apply {
                                                val currentAmount = getOrDefault(cleanEmail, 0.0)
                                                put(cleanEmail, currentAmount + paymentAmount)
                                            }
                                            group = group.copy(paidAmount = updatedPaidAmount)
                                            personalGroupViewModel.updateGroupInFirebase(group)

                                            // Reset the paymentAmount and dismiss the dialog
                                            paymentAmount = 0.0
                                            showDialogPay = false
                                        }
                                    }
                                ) {
                                    Text(text = "OK")
                                }
                            }
                        }
                    }
                )
            }

            // Add people Button
            Button(
                onClick = {
                          showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Add people")
            }

            if (showEditDialog) {
                Dialog(
                    onDismissRequest = {
                        showEditDialog = false
                        keyboardController?.hide()
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Change Group Name",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = textFieldValue,
                                onValueChange = {
                                    textFieldValue = it
                                },
                                label = { Text("New Group Name") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        group = group.copy(name = textFieldValue.text)
                                        personalGroupViewModel.updateGroupInFirebase(group)
                                        showEditDialog = false
                                        keyboardController?.hide()
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showEditDialog = false
                                        keyboardController?.hide()
                                    }
                                ) {
                                    Text(text = "Cancel")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                TextButton(
                                    onClick = {
                                        group = group.copy(name = textFieldValue.text)
                                        personalGroupViewModel.updateGroupInFirebase(group)
                                        showEditDialog = false
                                        keyboardController?.hide()
                                    }
                                ) {
                                    Text(text = "Save")
                                }
                            }
                        }
                    }
                )
            }

            // Dialog for adding people
            if (showDialog) {
                Dialog(
                    onDismissRequest = {
                        showDialog = false
                        // Hide the keyboard when the dialog is dismissed
                        keyboardController?.hide()
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Add People",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                            )

                            TextField(
                                value = textFieldValue,
                                onValueChange = { newValue ->
                                    textFieldValue = newValue
                                },
                                label = { Text("Enter email of person") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        showDialog = false
                                        keyboardController?.hide()
                                    }
                                ),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = contentColorFor(MaterialTheme.colorScheme.background)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        // Hide the keyboard when the "Cancel" button is clicked
                                        keyboardController?.hide()
                                    }
                                ) {
                                    Text(text = "Cancel")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                TextButton(
                                    onClick = {
                                        val emailToAdd = textFieldValue.text

                                        personalGroupViewModel.checkIfEmailExistsInFirebase(emailToAdd) { isEmailRegistered ->
                                            if (isEmailRegistered) {
                                                // Email is registered, add the participant
                                                participants = participants.toMutableList().apply {
                                                    add(emailToAdd)
                                                }

                                                // Fetch the associated name for the emailToAdd from the database
                                                personalGroupViewModel.fetchNameForEmail(emailToAdd) { associatedName ->
                                                    if (associatedName != null) {
                                                        // Use the associated name when creating the new Person
                                                        val newPerson = Person(emailToAdd, associatedName)
                                                        group = group.copy(members = group.members + newPerson)

                                                        val updatedPaidAmount = group.paidAmount.toMutableMap().apply {
                                                            put((emailToAdd.toString()).replace(".",""), 0.0)  // Assuming you want to initialize the new user with 0.0 paidAmount
                                                        }

                                                        group = group.copy(paidAmount = updatedPaidAmount)


                                                        // Update the group in the Firebase Realtime Database
                                                        personalGroupViewModel.updateGroupInFirebase(group)

                                                        // Update participants with the names of the current members
                                                        participants = group.members.map { it?.name ?: "" }

                                                        showDialog = false
                                                        // Hide the keyboard when the "Add" button is clicked
                                                        keyboardController?.hide()
                                                    } else {
                                                        // Handle the scenario when the associated name is not found
                                                        println("Associated name not found for the email: $emailToAdd")
                                                    }
                                                }
                                            } else {
                                                // Handle the scenario when the email is not registered
                                                // You might want to display an error message or take appropriate action
                                                // For now, let's just print a message
                                                println("Email is not registered in Firebase.")
                                            }
                                        }
                                    }
                                ) {
                                    Text(text = "Add")
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}