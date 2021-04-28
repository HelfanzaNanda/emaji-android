package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskItems(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("body")var body : String? = null
) : Parcelable