package com.example.share_money_now.firebaseauth.data

import com.example.share_money_now.firebaseauth.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(email:String, password:String, name:String): Flow<Resource<AuthResult>>

    fun logOut()
}