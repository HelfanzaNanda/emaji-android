package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskResult(
        @SerializedName("cycle_id")var cycle_id : Int? = null,
        @SerializedName("tool_id")var tool_id : Int? = null,
        @SerializedName("task_id")var task_id : Int? = null,
        @SerializedName("tasks") var tasks : List<TaskItems> = mutableListOf()
) : Parcelable