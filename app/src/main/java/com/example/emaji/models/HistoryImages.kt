package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryImages(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("filename") var filename : String? = null
) : Parcelable