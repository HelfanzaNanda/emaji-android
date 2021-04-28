package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tool(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("name")var name : String? = null,
    @SerializedName("merk")var merk : String? = null,
    @SerializedName("image")var image : String? = null
) : Parcelable