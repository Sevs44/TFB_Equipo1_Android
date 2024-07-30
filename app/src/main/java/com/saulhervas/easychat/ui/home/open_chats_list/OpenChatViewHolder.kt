package com.saulhervas.easychat.ui.home.open_chats_list

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.ItemUserRowMessageBinding
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.utils.DebouncedOnClickListener
import kotlin.random.Random

class OpenChatViewHolder(
    view: View,
    private val colorMap: MutableMap<String, Int>,
    private val context: Context
) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemUserRowMessageBinding.bind(view)

    fun onBind(
        itemChat: OpenChatItemModel?,
        onClickListener: (OpenChatItemModel?) -> Unit
    ) {
        binding.apply {
            val idTargetUser = itemChat?.idTargetUser ?: ""
            val userNameId: String =
                if (idTargetUser == "")
                    context.getString(R.string.unknown_user)
                else
                    idTargetUser
            val color = colorMap.getOrPut(userNameId) {
                generateRandomColor()
            }
            Log.i("TAG", "onBinddddddddddddd: $color")
            if (itemChat?.nickTargetUser == "") {
                tvUserName.text = getString(context, R.string.unknown_user)
            } else {
                if (itemChat != null) {
                    tvUserName.text = itemChat.nickTargetUser
                }
            }
            if (itemChat?.nickTargetUser == "") {
                tvInitial.text = "?"
            } else {
                if (itemChat != null) {
                    tvInitial.text = itemChat.nickTargetUser?.firstOrNull()?.uppercase().toString()
                }
            }
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
