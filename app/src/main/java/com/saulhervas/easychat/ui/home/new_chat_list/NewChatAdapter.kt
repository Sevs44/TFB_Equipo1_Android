package com.saulhervas.easychat.ui.home.new_chat_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.UserNewChatItemModel

class NewChatAdapter(
    private var items: MutableList<UserNewChatItemModel>,
    private val onClickListener: (UserNewChatItemModel?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userType = 1
    private val headerType = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == userType) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_row_message, parent, false)
            UserNameViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header_new_chat, parent, false)
            HeaderViewHolder(view)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == userType) {
            (holder as UserNameViewHolder).bind(
                items[position] as UserNewChatItemModel?,
                onClickListener
            )
        } else {
            (holder as HeaderViewHolder).bind(items[position] as String)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is UserNewChatItemModel) userType else headerType
    }

    fun getPositionForSection(letter: Char): Int {
        return items.indexOfFirst {
            (it.nick?.get(0) ?: ' ') == letter
        }
    }

    fun updateItems(newItems: List<UserNewChatItemModel>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

}