package com.example.emaji.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.User
import com.example.emaji.repositories.UserRepository
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse

open class ProfileViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val currentUser = MutableLiveData<User>()

    private fun isLoading(b : Boolean){ state.value = ProfileState.Loading(b)}
    private fun toast(m : String){ state.value = ProfileState.ShowToast(m) }

    fun fetchCurrentUser(token : String){
        isLoading(true)
        userRepository.currentUser(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { currentUser.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToCurrentUser() = currentUser


}

sealed class ProfileState{
    data class Loading(var state : Boolean = false) : ProfileState()
    data class ShowToast (var message : String) : ProfileState()
}