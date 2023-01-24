package com.shv.android.meetingreminder.presentation.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.domain.entity.Client
import com.shv.android.meetingreminder.domain.usecase.LoadClientsListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClientsViewModel @Inject constructor(
    private val loadClientsListUseCase: LoadClientsListUseCase
) : ViewModel() {

    private val _clientsList = MutableLiveData<List<Client>>()
    val clientsList: LiveData<List<Client>>
        get() = _clientsList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        viewModelScope.launch {
            val list = loadClientsListUseCase()
            _clientsList.value = list
            checkIsLoad()
        }
    }

    private fun checkIsLoad() {
        _isLoading.value = clientsList.value?.size == 0
    }
}