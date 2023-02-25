package com.shv.android.meetingreminder.di

import androidx.lifecycle.ViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.AddReminderViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.ClientsViewModel
import com.shv.android.meetingreminder.presentation.viewmodels.ReminderListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReminderListViewModel::class)
    fun bindReminderListViewModel(viewModel: ReminderListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddReminderViewModel::class)
    fun bindAddReminderViewModel(viewModel: AddReminderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClientsViewModel::class)
    fun bindClientsViewModel(viewModel: ClientsViewModel): ViewModel
}
