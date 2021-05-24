package com.example.emaji.repositories

import android.content.Context
import com.example.emaji.models.Task
import com.example.emaji.models.TaskResult
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.Constants
import com.example.emaji.utils.SingleResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import com.example.emaji.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.MultipartUploadTask
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

interface TaskContract{
    fun getTasks(token : String, cycleId : Int, toolId : Int, listener : SingleResponse<Task>)
    fun sendAnswer(token: String, taskResult: TaskResult, listener: SingleResponse<TaskResult>)
    fun sendAnswerImages(token: String, task_result_id : Int, images : Array<MultipartBody.Part?>, listener: SingleResponse<TaskResult>)
}

class TaskRepository (private val api : ApiService) : TaskContract{

    override fun getTasks(token: String, cycleId: Int, toolId: Int, listener: SingleResponse<Task>) {
        api.getTasks(token, cycleId, toolId).enqueue(object : Callback<WrappedResponse<Task>>{
            override fun onFailure(call: Call<WrappedResponse<Task>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Task>>, response: Response<WrappedResponse<Task>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun sendAnswer(
        token: String,
        taskResult: TaskResult,
        listener: SingleResponse<TaskResult>
    ) {
        val g = GsonBuilder().create()
        val json = g.toJson(taskResult)
        println(json)
        val b = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.sendTaskResult(token, b).enqueue(object :Callback<WrappedResponse<TaskResult>> {
            override fun onFailure(call: Call<WrappedResponse<TaskResult>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<TaskResult>>,
                response: Response<WrappedResponse<TaskResult>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun sendAnswerImages(token: String, task_result_id : Int, images: Array<MultipartBody.Part?>, listener: SingleResponse<TaskResult>) {
        api.sendTaskImagesResult(token, task_result_id, images).enqueue(object : Callback<WrappedResponse<TaskResult>>{
            override fun onFailure(call: Call<WrappedResponse<TaskResult>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<TaskResult>>,
                response: Response<WrappedResponse<TaskResult>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}