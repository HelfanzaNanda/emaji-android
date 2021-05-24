package com.example.emaji.webservices

import com.example.emaji.models.*
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @POST("tools/{id}/{name}/validate")
    fun validateTool(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Path("name") name : String
    ) : Call<WrappedResponse<Tool>>

    @GET("cycles/{toolId}")
    fun getCycles(
        @Header("Authorization") token : String,
        @Path("toolId") toolId : Int
    ) : Call<WrappedListResponse<Cycle>>

    @GET("tasks/{cycleId}/{toolId}")
    fun getTasks(
        @Header("Authorization") token : String,
        @Path("cycleId") cycleId : Int,
        @Path("toolId") toolId : Int
    ) : Call<WrappedResponse<Task>>

    @Headers("Content-Type: application/json")
    @POST("tasks/store")
    fun sendTaskResult(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<TaskResult>>

    @Multipart
    @POST("tasks/store/{task_result_id}/images")
    fun sendTaskImagesResult(
        @Header("Authorization") token : String,
        @Path("task_result_id") task_result_id : Int,
        @Part images : Array<MultipartBody.Part?>
    ) : Call<WrappedResponse<TaskResult>>


    @GET("files")
    fun fetchFiles(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<File>>

    @GET("history")
    fun fetchHistories(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<History>>

    @GET("user")
    fun currentUser(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/update/info")
    fun updateInfo(
        @Header("Authorization") token : String,
        @Field("name") name : String,
        @Field("email") email : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/update/password")
    fun updatePassword(
        @Header("Authorization") token : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

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