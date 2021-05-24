package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class HistoryItems(
        @SerializedName("id") var id : Int? = null,
        @SerializedName("task_item") var task_item : String? = null,
        @SerializedName("value") var value : String? = null
) : Parcelable