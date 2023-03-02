package com.shv.android.meetingreminder.di

import android.app.Application
import com.shv.android.meetingreminder.presentation.br.OnCompletedBroadcastReceiver
import com.shv.android.meetingreminder.presentation.br.RebootBroadcastReceiver
import com.shv.android.meetingreminder.presentation.fragments.AddReminderFragment
import com.shv.android.meetingreminder.presentation.fragments.ClientListFragment
import com.shv.android.meetingreminder.presentation.fragments.ReminderListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(fragment: ReminderListFragment)

    fun inject(fragment: AddReminderFragment)

    fun inject(fragment: ClientListFragment)

    fun inject(receiver: RebootBroadcastReceiver)

    fun inject(receiver: OnCompletedBroadcastReceiver)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}