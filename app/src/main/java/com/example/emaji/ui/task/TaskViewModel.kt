package com.example.emaji.ui.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.Task
import com.example.emaji.models.TaskItems
import com.example.emaji.models.TaskResult
import com.example.emaji.repositories.TaskRepository
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse

class TaskViewModel (private val taskRepository: TaskRepository) : ViewModel(){
    private val state : SingleLiveEvent<TaskState> = SingleLiveEvent()
    private val task = MutableLiveData<Task>()
    private val answerTaskItems = MutableLiveData<List<TaskItems>>()
    private val taskItems = MutableLiveData<List<TaskItems>>()
    private val taskId = MutableLiveData<Int>()

    private fun isLoading(b : Boolean) { state.value = TaskState.Loading(b) }
    private fun toast(m : String){ state.value = TaskState.ShowToast(m) }
    private fun alert(m : String){ state.value = TaskState.Alert(m) }
    private fun success() { state.value = TaskState.Success }

    fun getTasks(token : String, cycleId : String, toolId : String){
        isLoading(true)
        taskRepository.getTasks(token, cycleId.toInt(), toolId.toInt(), object : SingleResponse<Task>{
            override fun onSuccess(data: Task?) {
                isLoading(false)
                data?.let {
                    task.postValue(it)
                    taskItems.postValue(it.tasks)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun sendAnswer(token: String, taskResult: TaskResult){
        isLoading(true)
        taskRepository.sendAnswer(token, taskResult, object : SingleResponse<TaskResult>{
            override fun onSuccess(data: TaskResult?) {
                isLoading(false)
                data?.let {
                    success()
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                alert(err.message.toString())
            }

        })
    }

    fun checkedSelectedTask(taskItem: TaskItems, boolean: Boolean){
        val tempTasks = if (answerTaskItems.value == null){
            mutableListOf()
        }else{
            answerTaskItems.value as MutableList<TaskItems>
        }
        val sameTask = tempTasks.find {items ->
            items.id == taskItem.id
        }
        sameTask?.let {
            it.answer = boolean
        } ?: kotlin.run {
            taskItem.answer = boolean
            tempTasks.add(taskItem)
        }
        answerTaskItems.postValue(tempTasks)
    }

    fun validated(answers : List<TaskItems>) : Boolean{
        if (answers.size != taskItems.value!!.size){
            alert("harus di kerjakan semua")
            return false
        }
        return true
    }

    fun setTaskId(id : Int){
        taskId.value = id
    }


    fun listenToState() = state
    fun listenToTask() = task
    fun listenToAnswerTasks() = answerTaskItems
    fun gettaskId() = taskId
}

sealed class TaskState{
    data class Loading(var state : Boolean = false) : TaskState()
    data class ShowToast(var m : String) : TaskState()
    data class Alert(var m : String) : TaskState()
    object Success : TaskState()
}