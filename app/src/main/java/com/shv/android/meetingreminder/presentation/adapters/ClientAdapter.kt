package com.shv.android.meetingreminder.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shv.android.meetingreminder.R
import com.shv.android.meetingreminder.databinding.ClientItemBinding
import com.shv.android.meetingreminder.domain.entity.Client
import com.squareup.picasso.Picasso

class ClientAdapter(private val clientsList: List<Client>, private val context: Context) :
    RecyclerView.Adapter<ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding = ClientItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clientsList[position]
        with(holder.binding) {
            with(client) {
                val fullNameTemplate = context.resources.getString(R.string.full_name_template)

                Picasso.get().load(imgUrl).into(ivClientPhoto)
                tvClientName.text = String.format(
                    fullNameTemplate,
                    titleName,
                    firstName,
                    lastName
                )
                tvClientEmail.text = email
            }
        }
    }


    override fun getItemCount() = clientsList.size
}