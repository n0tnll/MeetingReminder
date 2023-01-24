package com.shv.android.meetingreminder.di

import android.app.Application
import com.shv.android.meetingreminder.data.ReminderRepositoryImpl
import com.shv.android.meetingreminder.data.database.AppDatabase
import com.shv.android.meetingreminder.data.database.ReminderDao
import com.shv.android.meetingreminder.data.network.api.ApiFactory
import com.shv.android.meetingreminder.data.network.api.ApiService
import com.shv.android.meetingreminder.domain.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideReminderDao(
            application: Application
        ): ReminderDao {
            return AppDatabase.newInstance(application).reminderDao()
        }

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}