package com.shv.android.meetingreminder.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shv.android.meetingreminder.domain.usecase.LoadClientsListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClientsViewModel @Inject constructor(
    private val loadClientsListUseCase: LoadClientsListUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StateClients>()
    val state: LiveData<StateClients>
        get() = _state

    init {
        loadClients()
    }

    fun loadClients() {
        val clientsList = viewModelScope.async {
            _state.value = Loading
            loadClientsListUseCase()
        }
        viewModelScope.launch {
            try {
                _state.value = ClientsList(clientsList.await())
            } catch (e: Exception) {
                _state.value = LoadingError
            }
        }
    }
}