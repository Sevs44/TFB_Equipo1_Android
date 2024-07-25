package com.saulhervas.easychat.ui.chat.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel

private const val VIEW_MESSAGE_RECEIVED = 1
private const val VIEW_MESSAGE_SENT = 2

class MessagesAdapter(
    val id: String
) : ListAdapter<MessageItemModel, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_MESSAGE_RECEIVED) {
            MessageReceivedViewHolder(
                layoutInflater.inflate(
                    R.layout.item_chat_from_row,
                    parent,
                    false
                )
            )
        } else {
            MessageSentViewHolder(
                layoutInflater.inflate(
                    R.layout.item_chat_to_row,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is MessageReceivedViewHolder) {
            holder.onBind(message)
        } else if (holder is MessageSentViewHolder) {
            holder.onBind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).sentBy == id) {
            VIEW_MESSAGE_SENT
        } else {
            VIEW_MESSAGE_RECEIVED
        }
    }
}