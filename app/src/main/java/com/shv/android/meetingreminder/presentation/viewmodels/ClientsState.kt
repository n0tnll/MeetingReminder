package com.shv.android.meetingreminder.presentation.viewmodels

import com.shv.android.meetingreminder.domain.entity.Client

sealed class StateClients

object Loading : StateClients()
object LoadingError : StateClients()
class ClientsList(
    val clientsList: List<Client> = arrayListOf()
) : StateClients()