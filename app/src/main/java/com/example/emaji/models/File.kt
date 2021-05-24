package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class File(
        @SerializedName("id") var id : Int? = null,
        @SerializedName("name") var name : String? = null,
        @SerializedName("filename") var filename : String? = null
) : Parcelable