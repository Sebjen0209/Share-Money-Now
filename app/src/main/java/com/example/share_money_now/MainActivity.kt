package com.example.share_money_now

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import com.example.share_money_now.ui.theme.ShareMoneyNowTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState)
        setContent {
            ShareMoneyNowTheme{
                Navigation()
            }
        }
    }
}
