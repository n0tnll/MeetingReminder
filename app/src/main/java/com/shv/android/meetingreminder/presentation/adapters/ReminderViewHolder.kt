package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shv.android.meetingreminder.databinding.ReminderItemBinding
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.shv.android.meetingreminder.presentation.adapters.ReminderAdapter.Companion.getDateFromMillisToString
import com.shv.android.meetingreminder.presentation.adapters.ReminderAdapter.Companion.getTimeFromMillisToString
import com.squareup.picasso.Picasso

class ReminderViewHolder(val binding: ReminderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(reminder: Reminder) {
        with(binding) {
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
    companion object {
        fun from(parent: ViewGroup) = ReminderViewHolder(
            ReminderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}