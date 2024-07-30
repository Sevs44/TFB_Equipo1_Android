package com.saulhervas.easychat.ui.home.open_chats_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.utils.DebouncedOnClickListener
import kotlin.random.Random

class OpenChatViewHolder(view: View, private val colorMap: MutableMap<String, Int>) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemUserRowMessageBinding.bind(view)

    fun onBind(
        itemChat: OpenChatItemModel?,
        onClickListener: (OpenChatItemModel?) -> Unit
    ) {
        binding.apply {
            val userName = itemChat?.nickTargetUser ?: ""
            val color = colorMap.getOrPut(userName) {
                generateRandomColor()
            }
            tvUserName.text = userName
            tvInitial.text = userName.firstOrNull()?.uppercase().toString()
            tvInitial.background = createCircleDrawable(color)
            itemView.setOnClickListener(object : DebouncedOnClickListener() {
                override fun onDebouncedClick(v: View) {
                    onClickListener(itemChat)
                }
            })
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

