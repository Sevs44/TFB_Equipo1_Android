package com.saulhervas.easychat.ui.new_chat.new_chat_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.UserNewChatItemModel

class NewChatAdapter(
    private val context: Context,
    private var itemList: MutableList<UserNewChatItemModel>,
    private val colorMap: MutableMap<String, Int>,
    private val onClickListener: (UserNewChatItemModel?) -> Unit
) : RecyclerView.Adapter<UserNameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_user_row_message, parent, false)
        return UserNameViewHolder(view, colorMap, context)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: UserNameViewHolder, position: Int) {
        val item = itemList[position]
        holder.onBind(item, onClickListener)
    }

    fun updateList(newItems: List<UserNewChatItemModel>) {
        itemList = newItems.toMutableList()
        notifyDataSetChanged()
    }

    fun getPositionForSection(letter: Char): Int {
        return itemList.indexOfFirst {
            (it.nick?.get(0) ?: ' ') == letter
        }
    }

}