package com.example.share_money_now.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.share_money_now.firebaseauth.data.AuthRepository
import com.example.share_money_now.firebaseauth.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()


    fun registerUser(email:String, password:String) = viewModelScope.launch {
        repository.loginUser(email, password).collect{result ->
            when(result){
                is Resource.Success -> {
                    _signUpState.send(SignUpState(isSuccess = "Sign In Succes"))
                }
                is Resource.Loading ->{
                    _signUpState.send(SignUpState(isLoading = true))
                }
                is Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message))
                }
            }
        }
    }

}