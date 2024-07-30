package com.saulhervas.easychat.ui.chat_detail

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentChatDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatDetailFragment : Fragment() {

    private lateinit var binding: FragmentChatDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatDetailBinding.inflate(inflater, container, false)
        onClickListener()
        requireContext()
        return binding.root
    }

    private fun onClickListener() {
        binding.btnBlock.setOnClickListener {
            showAlertStorage()
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showAlertStorage() {
        val customTitleLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(100, 50, 100, 30)
        }
        val customTitle = TextView(requireContext()).apply {
            text = getString(R.string.title_alert)
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        customTitleLayout.addView(customTitle)

        val dialog = AlertDialog.Builder(requireContext())
            .setCustomTitle(customTitleLayout)
            .setMessage(getString(R.string.message_alert))
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}