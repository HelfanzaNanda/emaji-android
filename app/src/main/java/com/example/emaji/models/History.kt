package com.example.emaji.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class History(
        @SerializedName("id") var id : Int?= null,
        @SerializedName("user_id") var user_id : Int?= null,
        @SerializedName("user_name") var user_name : String?= null,
        @SerializedName("cycle_id") var cycle_id : Int?= null,
        @SerializedName("cycle_name") var cycle_name : String?= null,
        @SerializedName("tool_id") var tool_id : Int?= null,
        @SerializedName("tool_name") var tool_name : String?= null,
        @SerializedName("tool_image") var tool_image : String?= null,
        @SerializedName("note") var note : String?= null,
        @SerializedName("history_items") var history_items : MutableList<HistoryItems> = mutableListOf(),
        @SerializedName("history_images") var history_images : MutableList<HistoryImages> = mutableListOf()
) : Parcelable
