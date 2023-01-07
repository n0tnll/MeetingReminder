package com.shv.android.meetingreminder.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.shv.android.meetingreminder.domain.entity.Client

class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
    override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
        return newItem.clientId == oldItem.clientId
    }

    override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
        return newItem == oldItem
    }
}