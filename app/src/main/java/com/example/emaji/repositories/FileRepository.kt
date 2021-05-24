package com.example.emaji.repositories

import com.example.emaji.models.File
import com.example.emaji.utils.ArrayResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FileContract{
    fun fetchFiles(token : String, listener : ArrayResponse<File>)
}

class FileRepository (private val api : ApiService) : FileContract{
    override fun fetchFiles(token: String, listener: ArrayResponse<File>) {
        api.fetchFiles(token).enqueue(object : Callback<WrappedListResponse<File>>{
            override fun onFailure(call: Call<WrappedListResponse<File>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<File>>, response: Response<WrappedListResponse<File>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()?.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}