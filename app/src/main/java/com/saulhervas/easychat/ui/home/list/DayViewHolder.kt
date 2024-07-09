package com.qualentum.sprint3.main.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.UserRowMessageBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel

class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = UserRowMessageBinding.bind(view)

    fun onBind(
        itemChat: OpenChatItemModel?
    //    , onClickListener: (OpenChatItemModel?) -> Unit
    ) {
        binding.apply {
            tvUserName.text = itemChat?.nickUser2
        }
    }
}