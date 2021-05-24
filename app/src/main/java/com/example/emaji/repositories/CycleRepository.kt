package com.example.emaji.repositories

import com.example.emaji.models.Cycle
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CycleContract{
    fun getCycles(token : String, toolId : Int, listener : ArrayResponse<Cycle>)
}

class CycleRepository (private val api : ApiService) : CycleContract{
    override fun getCycles(token: String, toolId : Int, listener: ArrayResponse<Cycle>) {
        api.getCycles(token, toolId).enqueue(object : Callback<WrappedListResponse<Cycle>>{
            override fun onFailure(call: Call<WrappedListResponse<Cycle>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Cycle>>,
                response: Response<WrappedListResponse<Cycle>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}