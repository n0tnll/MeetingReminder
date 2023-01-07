package com.shv.android.meetingreminder.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val clientId: String,
    val titleName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val imgUrl: String
) : Parcelable
