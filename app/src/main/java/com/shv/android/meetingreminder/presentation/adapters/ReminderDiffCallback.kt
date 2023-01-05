package com.shv.android.meetingreminder.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.shv.android.meetingreminder.domain.entity.Reminder

class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
    override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
        return newItem == oldItem
    }
}