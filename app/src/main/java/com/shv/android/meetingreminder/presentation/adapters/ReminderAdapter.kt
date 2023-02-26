package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.meetingreminder.databinding.ReminderItemBinding
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter :
    ListAdapter<Reminder, ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ReminderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = getItem(position)
        with(holder.binding) {
            with(reminder) {
                tvReminderTitle.text = title
                tvReminderDate.text = getDateFromMillisToString(dateTime)
                tvReminderTime.text = getTimeFromMillisToString(dateTime)
                tvClientName.text = fullName
                tvClientEmail.text = client.email
                Picasso.get().load(client.imgUrl).into(ivClientPhoto)
            }
        }
    }

    private fun getDateFromMillisToString(date: Long): String {
        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormatter.format(date)
    }

    private fun getTimeFromMillisToString(time: Long): String {
        val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormatter.format(time)
    }
}