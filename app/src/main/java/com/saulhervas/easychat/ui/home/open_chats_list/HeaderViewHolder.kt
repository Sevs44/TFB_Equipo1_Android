package com.saulhervas.easychat.ui.home.open_chats_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemHeaderNewChatBinding

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHeaderNewChatBinding.bind(view)

    fun bind(header: String) {
        binding.tvHeader.text = header
    }
}
