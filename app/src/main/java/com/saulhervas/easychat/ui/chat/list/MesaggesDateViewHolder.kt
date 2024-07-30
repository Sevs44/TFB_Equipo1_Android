package com.saulhervas.easychat.ui.chat.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemRowDateBinding
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel

class MesaggesDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemRowDateBinding.bind(view)

    fun onBind(
        messages: MessageItemModel?,
    ) {
        val messagesSent = messages?.messageSentAt
        binding.apply {
            dateTextView.text = messagesSent.toString()
        }
    }
}