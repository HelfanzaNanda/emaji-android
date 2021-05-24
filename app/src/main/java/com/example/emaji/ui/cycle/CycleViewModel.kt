package com.example.emaji.ui.cycle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.Cycle
import com.example.emaji.repositories.CycleRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent

class CycleViewModel (private var cycleRepository: CycleRepository) : ViewModel(){
    private val state : SingleLiveEvent<CycleState> = SingleLiveEvent()
    private val cycles = MutableLiveData<List<Cycle>>()
    private fun isLoading(b : Boolean) { state.value = CycleState.Loading(b) }
    private fun toast(m : String){ state.value = CycleState.ShowToast(m) }

    fun getCycles(token : String, toolId : Int){
        isLoading(true)
        cycleRepository.getCycles(token, toolId, object : ArrayResponse<Cycle>{
            override fun onSuccess(datas: List<Cycle>?) {
                isLoading(false)
                datas?.let {
                    cycles.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToCycles() = cycles
}

sealed class CycleState{
    data class Loading(var state : Boolean = false) : CycleState()
    data class ShowToast(var message : String) : CycleState()
}