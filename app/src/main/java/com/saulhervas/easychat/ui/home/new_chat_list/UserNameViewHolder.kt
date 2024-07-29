package com.saulhervas.easychat.ui.home.new_chat_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import kotlin.random.Random

class UserNameViewHolder(view: View, private val colorMap: MutableMap<String, Int>) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemUserRowMessageBinding.bind(view)

    fun bind(
        userNewChatItemModel: UserNewChatItemModel?,
        onClickListener: (UserNewChatItemModel?) -> Unit
    ) {
        binding.apply {
            val userName = userNewChatItemModel?.nick ?: ""
            val color = colorMap.getOrPut(userName) {
                generateRandomColor()
            }
            tvUserName.text = userName
            tvInitial.text = userName.firstOrNull()?.uppercase().toString()
            tvInitial.background = createCircleDrawable(color)
            itemView.setOnClickListener { onClickListener(userNewChatItemModel) }
        }
    }

    private fun generateRandomColor(): Int {
        val random = Random
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    private fun createCircleDrawable(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
    }
}