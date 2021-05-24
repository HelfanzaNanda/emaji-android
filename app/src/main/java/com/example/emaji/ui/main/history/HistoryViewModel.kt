package com.example.emaji.ui.main.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.History
import com.example.emaji.repositories.HistoryRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent

class HistoryViewModel (private val historyRepository: HistoryRepository) : ViewModel(){
    private val state : SingleLiveEvent<HistoryState> = SingleLiveEvent()
    private val histories = MutableLiveData<List<History>>()

    private fun isLoading(b : Boolean){ state.value = HistoryState.Loading(b) }
    private fun toast(m : String){ state.value = HistoryState.ShowToast(m) }

    fun fetchHistories(token : String){
        isLoading(true)
        historyRepository.fetchHistories(token, object : ArrayResponse<History>{
            override fun onSuccess(datas: List<History>?) {
                isLoading(false)
                datas?.let { histories.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToHistories() = histories
}

sealed class HistoryState{
    data class Loading(var state : Boolean = false) : HistoryState()
    data class ShowToast(var message : String) : HistoryState()
}