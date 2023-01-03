package com.shv.android.meetingreminder.domain.entity

data class Client(
    val clientId: Int,
    val titleName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imgUrl: String
)
