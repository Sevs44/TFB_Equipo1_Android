package com.saulhervas.easychat.ui.home.open_chats_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class OpenChatAdapter(
    private var itemList: MutableList<OpenChatItemModel>,
    private val colorMap: MutableMap<String, Int>,
    private val onClickListener: (OpenChatItemModel?) -> Unit
) : RecyclerView.Adapter<OpenChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_user_row_message, parent, false)
        return OpenChatViewHolder(view, colorMap)
    }

    override fun onBindViewHolder(holder: OpenChatViewHolder, position: Int) {
        val item = itemList[position]
        holder.onBind(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(newList: List<OpenChatItemModel>) {
        itemList = newList.toMutableList()
        notifyDataSetChanged()
    }
    fun getIdChat(position: Int): String? {
        return itemList[position].idChat
    }
}

