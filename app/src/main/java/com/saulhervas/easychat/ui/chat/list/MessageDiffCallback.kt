package com.saulhervas.easychat.ui.chat.list

import androidx.recyclerview.widget.DiffUtil
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel


class MessageDiffCallback : DiffUtil.ItemCallback<MessageItemModel>() {
    override fun areItemsTheSame(oldItem: MessageItemModel, newItem: MessageItemModel): Boolean {
        return oldItem.idMessage == newItem.idMessage
    }

    override fun areContentsTheSame(oldItem: MessageItemModel, newItem: MessageItemModel): Boolean {
        return oldItem == newItem
    }
}