package com.example.emaji.repositories

import com.example.emaji.models.Tool
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.utils.SingleResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import com.example.emaji.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface ToolContract{
    fun getTools(token : String, listener : ArrayResponse<Tool>)
    fun validateTool(token: String, id : Int, name: String, listener : SingleResponse<Tool>)
}

class ToolRepository (private val api : ApiService) : ToolContract{
    override fun getTools(token: String, listener: ArrayResponse<Tool>) {
        api.getTools(token).enqueue(object : Callback<WrappedListResponse<Tool>>{
            override fun onFailure(call: Call<WrappedListResponse<Tool>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Tool>>,
                response: Response<WrappedListResponse<Tool>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun validateTool(token: String, id: Int, name: String, listener: SingleResponse<Tool>) {
        api.validateTool(token, id, name).enqueue(object : Callback<WrappedResponse<Tool>>{
            override fun onFailure(call: Call<WrappedResponse<Tool>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Tool>>,
                response: Response<WrappedResponse<Tool>>
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