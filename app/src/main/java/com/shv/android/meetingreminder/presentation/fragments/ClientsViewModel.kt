package com.shv.android.meetingreminder.presentation.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.data.ReminderRepositoryImpl
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.usecase.LoadClientsListUseCase
import kotlinx.coroutines.launch

class ClientsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReminderRepositoryImpl(application)
    private val loadClientsListUseCase = LoadClientsListUseCase(repository)

    private val _clientsList = MutableLiveData<List<Client>>()
    val clientsList: LiveData<List<Client>>
        get() = _clientsList

    init {
        viewModelScope.launch {
            val list = loadClientsListUseCase()
            _clientsList.value = list
        }
    }
}