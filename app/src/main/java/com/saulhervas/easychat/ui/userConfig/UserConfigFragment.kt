package com.saulhervas.easychat.ui.userConfig

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
import com.saulhervas.easychat.databinding.FragmentUserConfigBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserConfigFragment : Fragment() {
    private lateinit var binding: FragmentUserConfigBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserConfigBinding.inflate(inflater, container, false)
        setOnClickListener()
        loadImageUri()

        setupUI(binding.root)
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnProfile.setOnClickListener {
            Log.d("ProfileSettingsFragment", "boton perfil")
            findNavController().navigate(R.id.action_userConfig_to_profileSettingsFragment)
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCloseSession.setOnClickListener {
            findNavController().navigate(R.id.action_userConfig_to_userLogoutFragment)
        }
    }

    private fun observeViewModel() {
        // Aquí puedes observar cambios en el ViewModel si es necesario
    }


    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        // Configurar listener para ocultar el teclado
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
