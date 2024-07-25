package com.saulhervas.easychat.ui.home.list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import kotlin.random.Random

class OpenChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemUserRowMessageBinding.bind(view)

    fun onBind(
        itemChat: OpenChatItemModel?,
        onClickListener: (OpenChatItemModel?) -> Unit
    ) {
        binding.apply {
            tvUserName.text = itemChat?.nickTargetUser
            tvInitial.text = itemChat?.nickTargetUser?.firstOrNull()?.uppercase().toString() ?: ""
            tvInitial.background = generateRandomCircleDrawable()
            itemView.setOnClickListener { onClickListener(itemChat) }
        }
    }

    private fun generateRandomCircleDrawable(): GradientDrawable {
        val random = Random
        val color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))

        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
        }
    }
}