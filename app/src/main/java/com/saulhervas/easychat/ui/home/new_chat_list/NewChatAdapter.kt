package com.saulhervas.easychat.ui.home.new_chat_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.UserNewChatItemModel

class NewChatAdapter(
    private val items: List<Any>,
    private val context: Context,
    private val onClickListener: (UserNewChatItemModel?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val USER_TYPE = 1
    private val HEADER_TYPE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == USER_TYPE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_new_chat, parent, false)
            UserNameViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header_new_chat, parent, false)
            HeaderViewHolder(view)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == USER_TYPE) {
            (holder as UserNameViewHolder).bind(
                items[position] as UserNewChatItemModel?,
                onClickListener
            )
        } else {
            (holder as HeaderViewHolder).bind(items[position] as String)
        }
    }

}