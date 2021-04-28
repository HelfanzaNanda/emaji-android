package com.example.emaji.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.Task
import com.example.emaji.repositories.TaskRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent

class TaskViewModel (private val taskRepository: TaskRepository) : ViewModel(){
    private val state : SingleLiveEvent<TaskState> = SingleLiveEvent()
    private val tasks = MutableLiveData<List<Task>>()

    private fun isLoading(b : Boolean) { state.value = TaskState.Loading(b) }
    private fun toast(m : String){ state.value = TaskState.ShowToast(m) }
    private fun success() { state.value = TaskState.Success }

    fun getTasks(token : String, cycleId : String, toolId : String){
        isLoading(true)
        taskRepository.getTasks(token, cycleId.toInt(), toolId.toInt(), object : ArrayResponse<Task>{
            override fun onSuccess(datas: List<Task>?) {
                isLoading(false)
                datas?.let {
                    tasks.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToTasks() = tasks
}

sealed class TaskState{
    data class Loading(var state : Boolean = false) : TaskState()
    data class ShowToast(var m : String) : TaskState()
    object Success : TaskState()
}