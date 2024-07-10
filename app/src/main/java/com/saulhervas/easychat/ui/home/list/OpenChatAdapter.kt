package com.qualentum.sprint3.main.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class OpenChatAdapter(
    val itemList: MutableList<OpenChatItemModel>?,
    private val onClickListener: () -> Unit
) : RecyclerView.Adapter<OpenChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OpenChatViewHolder(layoutInflater.inflate(R.layout.user_row_message, parent, false))
    }

    override fun onBindViewHolder(holder: OpenChatViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.onBind(
            item,
            onClickListener
        )
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

}