package com.shv.android.meetingreminder.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClientNameDto(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("first")
    @Expose
    val first: String,

    @SerializedName("last")
    @Expose
    val last: String
)