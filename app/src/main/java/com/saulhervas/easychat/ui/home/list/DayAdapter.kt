package com.qualentum.sprint3.main.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class DayAdapter(
    val itemList: MutableList<OpenChatItemModel>?
//, private val onClickListener: (OpenChatItemModel?) -> Unit
) : RecyclerView.Adapter<DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DayViewHolder(layoutInflater.inflate(R.layout.user_row_message, parent, false))
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.onBind(
            item
            //,onClickListener
        )
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

}