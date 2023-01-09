package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.meetingreminder.databinding.ReminderItemBinding
import com.shv.android.meetingreminder.domain.entity.Reminder
import com.squareup.picasso.Picasso

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
                tvReminderDate.text = date
                tvReminderTime.text = time
                tvClientName.text = fullName
                tvClientEmail.text = client.email
                Picasso.get().load(client.imgUrl).into(ivClientPhoto)
            }
        }
    }
}