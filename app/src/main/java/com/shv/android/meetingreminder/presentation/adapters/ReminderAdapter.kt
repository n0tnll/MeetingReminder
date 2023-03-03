package com.shv.android.meetingreminder.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shv.android.meetingreminder.domain.entity.Reminder
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter :
    ListAdapter<Reminder, ViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_STATE_NOT_COMPLETED) {
            ReminderViewHolder.from(parent)
        } else {
            ReminderCompletedViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = getItem(position)
        when (holder) {
            is ReminderViewHolder -> holder.bind(reminder)
            is ReminderCompletedViewHolder -> holder.bind(reminder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).status) {
            ITEM_STATE_COMPLETED
        } else {
            ITEM_STATE_NOT_COMPLETED
        }
    }

    companion object {
        const val ITEM_STATE_COMPLETED = 100
        const val ITEM_STATE_NOT_COMPLETED = 101

        fun getDateFromMillisToString(date: Long): String {
            val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return dateFormatter.format(date)
        }

        fun getTimeFromMillisToString(time: Long): String {
            val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return dateFormatter.format(time)
        }
    }
}