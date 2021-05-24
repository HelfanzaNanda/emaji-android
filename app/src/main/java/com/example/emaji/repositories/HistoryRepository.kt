package com.example.emaji.repositories

import com.example.emaji.models.History
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface HistoryContact{
    fun fetchHistories(token : String, listener : ArrayResponse<History>)
}

class HistoryRepository (private val api : ApiService) : HistoryContact{
    override fun fetchHistories(token: String, listener: ArrayResponse<History>) {
        api.fetchHistories(token).enqueue(object : Callback<WrappedListResponse<History>>{
            override fun onFailure(call: Call<WrappedListResponse<History>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<History>>, response: Response<WrappedListResponse<History>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}