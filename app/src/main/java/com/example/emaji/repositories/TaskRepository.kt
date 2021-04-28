package com.example.emaji.repositories

import com.example.emaji.models.Task
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface TaskContract{
    fun getTasks(token : String, cycleId : Int, toolId : Int, listener : ArrayResponse<Task>)
}

class TaskRepository (private val api : ApiService) : TaskContract{
    override fun getTasks(token: String, cycleId: Int, toolId: Int, listener: ArrayResponse<Task>) {
        api.getTasks(token, cycleId, toolId).enqueue(object : Callback<WrappedListResponse<Task>>{
            override fun onFailure(call: Call<WrappedListResponse<Task>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Task>>,
                response: Response<WrappedListResponse<Task>>
            ) {
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

}