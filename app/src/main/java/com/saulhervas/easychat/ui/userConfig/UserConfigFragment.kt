package com.saulhervas.easychat.ui.userConfig

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saulhervas.easychat.databinding.FragmentUserConfigBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserConfigFragment : Fragment() {
    private lateinit var binding: FragmentUserConfigBinding
    private val args: UserConfigFragmentArgs by navArgs()
    private lateinit var token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserConfigBinding.inflate(inflater, container, false)
        setOnClickListener()

        setupUI(binding.root)
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnProfile.setOnClickListener {
            Log.d("ProfileSettingsFragment", "boton perfil")
            val action = UserConfigFragmentDirections.actionUserConfigToProfileSettingsFragment(
                token,
                id.toString()
            )
            findNavController().navigate(action)
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun observeViewModel() {
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

        // Si una vista es un contenedor, repetir para sus hijos
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

    private fun getUserArgs() {
        token = args.token
    }
}
