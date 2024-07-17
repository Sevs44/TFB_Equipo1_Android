package com.saulhervas.easychat.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentProfileSettingsBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        setOnClickListener()
        loadImageUri()
        setupUI(binding.root)
        return binding.root
    }

    private fun setOnClickListener() {
        binding.tvEditPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_profileSettingsFragment_to_photoEditFragment)
        }
        binding.ivProfile.setOnClickListener {
            Log.d("ProfileSettingsFragment", "ivProfile clicked")
            findNavController().navigate(R.id.action_profileSettingsFragment_to_photoEditFragment)
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    private fun observeViewModel() {
        // Observa los cambios en el ViewModel si es necesario
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
