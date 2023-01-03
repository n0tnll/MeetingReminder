package com.shv.android.meetingreminder.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IdDto(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("value")
    @Expose
    val value: String
)