package com.example.emaji.ui.login

import androidx.lifecycle.ViewModel
import com.example.emaji.models.User
import com.example.emaji.repositories.UserRepository
import com.example.emaji.utils.Constants
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse

class LoginViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value = LoginState.Loading(b) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun success(token: String){ state.value = LoginState.Success(token) }

    fun validate(email: String, password : String) : Boolean{
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "Email tidak valid")
            return false
        }
        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    fun login(email: String, password: String){
        isLoading(true)
        userRepository.login(email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    success(it.token!!)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Success(var token : String) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
    object Reset : LoginState()
}