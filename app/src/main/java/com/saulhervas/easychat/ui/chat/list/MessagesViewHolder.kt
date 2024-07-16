package com.saulhervas.easychat.ui.chat.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemChatFromRowBinding
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel

class MessagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemChatFromRowBinding.bind(view)

    fun onBind(
        messages: MessageItemModel?,
    ) {
        binding.apply {
            tvMessageFrom.text = messages?.messageContent
        }
    }
}