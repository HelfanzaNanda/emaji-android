package com.example.emaji.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.Tool
import com.example.emaji.repositories.ToolRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse
import kotlin.math.log

class HomeViewModel (private val toolRepository: ToolRepository) : ViewModel(){
    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val tools = MutableLiveData<List<Tool>>()
    private fun isLoading(b : Boolean) { state.value = HomeState.Loading(b) }
    private fun toast(m : String){ state.value = HomeState.ShowToast(m) }
    private fun successScan(toolId: Int) { state.value = HomeState.SuccessScan(toolId) }

    fun getTools(token : String){
        isLoading(true)
        toolRepository.getTools(token, object : ArrayResponse<Tool>{
            override fun onSuccess(datas: List<Tool>?) {
                isLoading(false)
                datas?.let {
                    tools.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun validatedTool(token: String, id :String){
        println("tool id"+id)
        isLoading(true)
        toolRepository.validateTool(token, id.toInt(), object : SingleResponse<Tool>{
            override fun onSuccess(data: Tool?) {
                isLoading(false)
                println("success" +data)
                data?.let {
                    println("data success"+data)
                    successScan(id.toInt())
                }
            }

            override fun onFailure(err: Error) {
                println("error "+err)
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToTools() = tools
}

sealed class HomeState{
    data class SuccessScan(var toolId : Int) : HomeState()
    data class Loading(var state : Boolean = false) : HomeState()
    data class ShowToast(var message : String) : HomeState()
}