package com.shv.android.meetingreminder.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClientDto(
    @SerializedName("name")
    @Expose
    val clientNameDto: ClientNameDto,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("picture")
    @Expose
    val clientPictures: ClientPictures
)