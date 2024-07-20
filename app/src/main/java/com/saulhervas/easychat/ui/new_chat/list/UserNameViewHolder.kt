package com.saulhervas.easychat.ui.new_chat.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserNewChatBinding
import com.saulhervas.easychat.domain.model.UserItemModel

class UserNameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemUserNewChatBinding.bind(view)

    fun bind(
        userItemModel: UserItemModel?,
        onClickListener: (UserItemModel?) -> Unit
    ) {
        binding.tvUserName.text = userItemModel?.nick
        itemView.setOnClickListener { onClickListener(userItemModel) }
    }
}