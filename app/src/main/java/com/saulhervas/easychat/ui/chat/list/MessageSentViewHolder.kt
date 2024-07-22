package com.saulhervas.easychat.ui.chat.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemChatToRowBinding
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel

class MessageSentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemChatToRowBinding.bind(view)

    fun onBind(
        messages: MessageItemModel?,
    ) {
        binding.apply {
            tvMessageTo.text = messages?.messageContent
            tvDateMessage.text = messages?.messageSentAt
        }
    }
}