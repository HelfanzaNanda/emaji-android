package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("cycle_id")var cycle_id : Int? = null,
    @SerializedName("cycle_name")var cycle_name : String? = null,
    @SerializedName("tool_id")var tool_id : Int? = null,
    @SerializedName("tool_name")var tool_name : String? = null,
    @SerializedName("tool_used")var tools_used : String? = null,
    @SerializedName("tasks") var tasks : MutableList<TaskItems> = mutableListOf()
) : Parcelable