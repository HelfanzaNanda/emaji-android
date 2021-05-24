package com.example.emaji.ui.update_password

import androidx.lifecycle.ViewModel
import com.example.emaji.models.User
import com.example.emaji.repositories.UserRepository
import com.example.emaji.utils.Constants
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse

class UpdatePasswordViewModel (private val userRepository: UserRepository) : ViewModel(){

    private val state : SingleLiveEvent<UpdatePasswordState> = SingleLiveEvent()

    private fun isLoading(b : Boolean){ state.value = UpdatePasswordState.Loading(b) }
    private fun toast(m : String){ state.value = UpdatePasswordState.ShowToast(m) }
    private fun success(){ state.value = UpdatePasswordState.Success }

    fun validate(password: String, confirm_password: String) : Boolean{
        state.value = UpdatePasswordState.Reset
        if (password.isEmpty()){
            state.value = UpdatePasswordState.Validate(password = "password tidak boleh kosong")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = UpdatePasswordState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }

        if (!password.equals(confirm_password)){
            state.value = UpdatePasswordState.Validate(confirm_password = "konfirmasi password harus sama dengan password")
            return false
        }
        return true
    }

    fun updatePassword(token : String, password: String){
        isLoading(true)
        userRepository.updatePassword(token, password, object : SingleResponse<User> {
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let {
                    success()
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

sealed class UpdatePasswordState {
    data class Loading(var state : Boolean) : UpdatePasswordState()
    data class ShowToast(var message : String) : UpdatePasswordState()
    object Success : UpdatePasswordState()
    data class Validate(var password : String? = null, var confirm_password: String? = null) : UpdatePasswordState()
    object Reset : UpdatePasswordState()
}