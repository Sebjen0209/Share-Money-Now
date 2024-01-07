package com.example.share_money_now

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.share_money_now.ui.theme.ShareMoneyNowTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
            ShareMoneyNowTheme {
                FirebaseAuth.getInstance().currentUser?.let { user ->
                    val uid = user.uid
                    val email = user.email
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result
                                if (token != null && email != null) {
                                    updateTokenInFirestore(uid, email, token)
                                }
                            }
                        })
                }
            }
        }
    }
    private fun updateTokenInFirestore(uid: String, email: String, token: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userTokenMap = mapOf(
            "notificationToken" to token,
            "email" to email
        )
        firestore.collection("persons").document(uid)
            .set(userTokenMap, SetOptions.merge())
    }
}