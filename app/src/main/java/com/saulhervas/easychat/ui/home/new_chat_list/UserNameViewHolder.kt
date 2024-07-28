package com.saulhervas.easychat.ui.home.new_chat_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.UserNewChatItemModel

class UserNameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemUserRowMessageBinding.bind(view)

    fun bind(
        userNewChatItemModel: UserNewChatItemModel?,
        onClickListener: (UserNewChatItemModel?) -> Unit
    ) {
        binding.tvUserName.text = userNewChatItemModel?.nick
        itemView.setOnClickListener { onClickListener(userNewChatItemModel) }
    }
}