package com.example.emaji.ui.update_profile

import androidx.lifecycle.ViewModel
import com.example.emaji.models.User
import com.example.emaji.repositories.UserRepository
import com.example.emaji.utils.Constants
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse

class UpdateProfileViewModel (private val userRepository: UserRepository) : ViewModel(){

    private val state : SingleLiveEvent<UpdateProfileState> = SingleLiveEvent()

    private fun isLoading(b : Boolean){ state.value = UpdateProfileState.Loading(b) }
    private fun toast(m : String){ state.value = UpdateProfileState.ShowToast(m) }
    private fun success() { state.value = UpdateProfileState.Success }

    fun validate(name: String, email: String) : Boolean{
        state.value = UpdateProfileState.Reset
        if (name.isEmpty()){
            state.value = UpdateProfileState.Validate(name = "nama tidak boleh kosong")
            return false
        }
        if (email.isEmpty()){
            state.value = UpdateProfileState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = UpdateProfileState.Validate(email = "Email tidak valid")
            return false
        }
        return true
    }

    fun updateProfile(token : String, name: String, email: String){
        isLoading(true)
        userRepository.updateProfile(token, name, email, object : SingleResponse<User>{
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

sealed class UpdateProfileState {
    data class Loading(var state : Boolean) : UpdateProfileState()
    data class ShowToast(var message : String) : UpdateProfileState()
    object Success : UpdateProfileState()
    data class Validate(var name : String? = null, var email: String? = null) : UpdateProfileState()
    object Reset : UpdateProfileState()
}