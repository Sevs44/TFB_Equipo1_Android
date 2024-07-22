package com.saulhervas.easychat.ui.home.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class OpenChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemUserRowMessageBinding.bind(view)

    fun onBind(
        itemChat: OpenChatItemModel?,
        onClickListener: (OpenChatItemModel?) -> Unit
    ) {
        binding.apply {
            tvUserName.text = itemChat?.nickUser2
            itemView.setOnClickListener { onClickListener(itemChat) }
        }
    }
}