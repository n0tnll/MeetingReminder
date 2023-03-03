package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shv.android.meetingreminder.databinding.ReminderItemCompletedBinding
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.squareup.picasso.Picasso

class ReminderCompletedViewHolder(val binding: ReminderItemCompletedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(reminder: Reminder) {
        with(binding) {
            with(reminder) {
                tvReminderTitle.text = title
                tvReminderDate.text = ReminderAdapter.getDateFromMillisToString(dateTime)
                tvReminderTime.text = ReminderAdapter.getTimeFromMillisToString(dateTime)
                tvClientName.text = fullName
                tvClientEmail.text = client.email
                Picasso.get().load(client.imgUrl).into(ivClientPhoto)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ReminderCompletedViewHolder(
            ReminderItemCompletedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}