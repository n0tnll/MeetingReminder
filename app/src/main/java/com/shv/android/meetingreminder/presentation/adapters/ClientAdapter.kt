package com.shv.android.meetingreminder.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shv.android.meetingreminder.R
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
        val context = holder.itemView.context
        with(holder.binding) {
            with(client) {
                val fullNameTemplate = context.resources.getString(R.string.full_name_template)

                Picasso.get().load(imgUrl).fit().into(ivClientPhoto)
                tvClientName.text = String.format(
                    fullNameTemplate,
                    titleName,
                    firstName,
                    lastName
                )
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