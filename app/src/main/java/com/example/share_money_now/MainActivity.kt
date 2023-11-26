package com.example.share_money_now

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.share_money_now.ui.theme.ShareMoneyNowTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
            ShareMoneyNowTheme {
              FirebaseMessaging.getInstance().token
                  .addOnCompleteListener(OnCompleteListener { task ->
                      if (!task.isSuccessful) {
                          Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                          return@OnCompleteListener
                      }

                      //Get new FCM registration token
                      val token: String? = task.result
                      Log.d("FCM Token", token, task.exception)
                      //Toast.makeText(this, token, Toast.LENGTH_SHORT).show() --- DISPLAY TOKEN ONSCREEN
                  })
            }
        }
    }
}