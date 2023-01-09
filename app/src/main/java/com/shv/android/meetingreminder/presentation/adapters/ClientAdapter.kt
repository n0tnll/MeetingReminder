package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.meetingreminder.databinding.ClientItemBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.squareup.picasso.Picasso

class ClientAdapter :
    ListAdapter<Client, ClientViewHolder>(ClientDiffCallback()) {

    var onClientClickListener: OnClientClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding = ClientItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = getItem(position)
        with(holder.binding) {
            with(client) {
                Picasso.get().load(imgUrl).fit().into(ivClientPhoto)
                tvClientName.text = fullName
                tvClientEmail.text = email
            }
            root.setOnClickListener {
                onClientClickListener?.onClientClick(client)
            }
        }
    }

    interface OnClientClickListener {
        fun onClientClick(client: Client)
    }
}