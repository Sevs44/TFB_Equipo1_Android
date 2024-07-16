package com.saulhervas.easychat.ui.chat.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel

class MessagesAdapter(
    val messages: MutableList<MessageItemModel>?
) : RecyclerView.Adapter<MessagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MessagesViewHolder(
            layoutInflater.inflate(
                R.layout.item_chat_from_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val item = messages?.get(position)
        holder.onBind(
            item
        )
    }

    override fun getItemCount(): Int {
        return messages?.size ?: 0
    }

}