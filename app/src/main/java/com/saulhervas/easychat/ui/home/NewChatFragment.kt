package com.saulhervas.easychat.ui.home

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.saulhervas.easychat.databinding.FragmentNewChatBinding
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import com.saulhervas.easychat.ui.home.new_chat_list.NewChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewChatFragment : Fragment() {

    private val args: NewChatFragmentArgs by navArgs()
    private val idUser: String = args.idUser

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentNewChatBinding
    private lateinit var newChatAdapter: NewChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewChatBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

    }

    private fun setOnClickListener() {
        binding.imBtnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        updateLists()
        lifecycleScope.launch {
            viewModel.newChatsState.collect {
                setUpRecyclerView(viewModel.getFilteredList(it))
            }
        }
    }

    private fun updateLists() {
        viewModel.getOpenChats()
        viewModel.getUserList()
        viewModel.filterUsers(idUser)
    }

    private fun setUpRecyclerView(userList: List<UserNewChatItemModel>) {
        binding.rvUsersNewChat.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsersNewChat.adapter =
            NewChatAdapter(userList) { user ->
                showCreateChatDialog(user)
            }
        //setUpAlphabetScroller()
    }

    private fun showCreateChatDialog(newChatItemModel: UserNewChatItemModel?) {
        //HACER STRINGS
        val message = "Crear Chat"
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                newChatItemModel?.id?.let { idTarget ->
                    viewModel.createChat(
                        idUser = idUser,
                        idTarget = idTarget
                    )
                    lifecycleScope.launch {
                        viewModel.isChatCreatedState.collect {
                            if (it)
                                findNavController().navigateUp()
                            else
                                Toast.makeText(
                                    requireContext(),
                                    "Chat no creado",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()

    }

    private fun setUpAlphabetScroller() {
        val letters = ('A'..'Z')
        for (letter in letters) {
            val textView = TextView(requireContext()).apply {
                text = letter.toString()
                textSize = 16f
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                setOnClickListener {
                    scrollToLetter(letter, this)
                }
            }
            binding.alphabetScroller.addView(textView)
        }
    }

    private fun scrollToLetter(letter: Char, tv: TextView) {
        val position = newChatAdapter.getPositionForSection(letter)
        if (position >= 0) {
            highlightLetter(tv, Color.CYAN)
            val smoothScroller: RecyclerView.SmoothScroller =
                object : LinearSmoothScroller(requireContext()) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position
            binding.rvUsersNewChat.layoutManager?.startSmoothScroll(smoothScroller)
        } else
            highlightLetter(tv, Color.RED)
    }

    private fun highlightLetter(textView: TextView, color: Int) {
        val originalColor = textView.currentTextColor

        val colorAnimator = ValueAnimator.ofArgb(color, originalColor).apply {
            duration = 1000
            addUpdateListener { animator ->
                textView.setTextColor(animator.animatedValue as Int)
            }
            doOnEnd {
                textView.setTextColor(originalColor)
            }
        }

        textView.setTextColor(color)
        Handler(Looper.getMainLooper()).postDelayed({
            colorAnimator.start()
        }, 1000)
    }

}