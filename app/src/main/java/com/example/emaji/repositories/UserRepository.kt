package com.example.emaji.repositories

import com.example.emaji.models.User
import com.example.emaji.utils.SingleResponse
import com.example.emaji.webservices.ApiService
import com.example.emaji.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface UserContract{
    fun login(email : String, password : String, listener : SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract{
    override fun login(email: String, password: String, listener: SingleResponse<User>) {
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
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