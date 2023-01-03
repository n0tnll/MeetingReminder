package com.shv.android.meetingreminder.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClientsListDto(
    @SerializedName("results")
    @Expose
    val clients: List<ClientDto>
)