package com.example.emaji.webservices

import com.example.emaji.models.Cycle
import com.example.emaji.models.Task
import com.example.emaji.models.Tool
import com.example.emaji.models.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface ApiService{
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @GET("tools")
    fun getTools(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Tool>>

    @POST("tools/{id}/validate")
    fun validateTool(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Tool>>

    @GET("cycles")
    fun getCycles(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Cycle>>

    @GET("tasks/{cycleId}/{toolId}")
    fun getTasks(
        @Header("Authorization") token : String,
        @Path("cycleId") cycleId : Int,
        @Path("toolId") toolId : Int
    ) : Call<WrappedListResponse<Task>>

}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)