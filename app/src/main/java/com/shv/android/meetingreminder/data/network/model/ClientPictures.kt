package com.shv.android.meetingreminder.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClientPictures(
    @SerializedName("large")
    @Expose
    val large: String,

    @SerializedName("medium")
    @Expose
    val medium: String,

    @SerializedName("thumbnail")
    @Expose
    val thumbnail: String
)