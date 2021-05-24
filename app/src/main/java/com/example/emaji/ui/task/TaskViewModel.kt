package com.example.emaji.ui.task

import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emaji.models.Task
import com.example.emaji.models.TaskItems
import com.example.emaji.models.TaskResult
import com.example.emaji.repositories.TaskRepository
import com.example.emaji.utils.SingleLiveEvent
import com.example.emaji.utils.SingleResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class TaskViewModel (private val taskRepository: TaskRepository) : ViewModel(){
    private val state : SingleLiveEvent<TaskState> = SingleLiveEvent()
    private val task = MutableLiveData<Task>()
    private val answerTaskItems = MutableLiveData<List<TaskItems>>()
    private val taskItems = MutableLiveData<List<TaskItems>>()
    private val taskId = MutableLiveData<Int>()
    private var images = MutableLiveData<MutableMap<String, String>>()

    init {
        images.value = mutableMapOf()
    }

    private fun isLoading(b : Boolean) { state.value = TaskState.Loading(b) }
    private fun toast(m : String){ state.value = TaskState.ShowToast(m) }
    private fun alert(m : String){ state.value = TaskState.Alert(m) }
    private fun success() { state.value = TaskState.Success }
    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

    fun addImage(key : String, v : String){
        images.value!![key] = v
    }

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

    private fun setMultipartImages(): Array<MultipartBody.Part?> {
        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(images.value!!.size)
        var i = 0
        images.value!!.forEach{ (_, value) ->
            val file = File(value)
            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            multipartTypedOutput[i] = MultipartBody.Part.createFormData("images[$i]", file.name, body)
            i++
        }
        return multipartTypedOutput
    }

    private fun setRequestBody(taskResult: TaskResult): HashMap<String, RequestBody> {
        val map = HashMap<String, RequestBody>()
        map["cycle_id"] = createPartFromString(taskResult.cycle_id.toString())
        map["tool_id"] = createPartFromString(taskResult.tool_id.toString())
        map["task_id"] = createPartFromString(taskResult.task_id.toString())
        map["note"] = createPartFromString(taskResult.note!!)
        map["tasks"] = RequestBody.create(MediaType.parse("text/plain"), taskResult.tasks.toString())
        return map
    }

    fun sendAnswer(token: String, taskResult: TaskResult){
        isLoading(true)
        taskRepository.sendAnswer(token, taskResult, object : SingleResponse<TaskResult>{
            override fun onSuccess(data: TaskResult?) {
                data?.let {
                    if (images.value?.isNotEmpty()!!){
                        sendImages(token, it.id!!)
                    }else{
                        isLoading(false)
                        success()
                    }
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun sendImages(token: String, task_result_id : Int){
        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(images.value!!.size)
        var i = 0
        images.value!!.forEach{ (_, value) ->
            val file = File(value)
            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            multipartTypedOutput[i] = MultipartBody.Part.createFormData("images[$i]", file.name, body)
            i++
        }
        taskRepository.sendAnswerImages(token, task_result_id, multipartTypedOutput, object : SingleResponse<TaskResult>{
            override fun onSuccess(data: TaskResult?) {
                data?.let {
                    isLoading(false)
                    success()
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
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