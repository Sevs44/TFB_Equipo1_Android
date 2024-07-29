package com.saulhervas.easychat.ui.userConfig

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentUserConfigBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserConfigFragment : Fragment() {
    private lateinit var binding: FragmentUserConfigBinding
    private val viewModel: UserConfigViewModel by viewModels()

    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserConfigBinding.inflate(inflater, container, false)
        setUpProfileBaseImage()
        setOnClickListener()
        setupUI(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.getUserConfig()
    }

    private fun setOnClickListener() {
        binding.btnProfile.setOnClickListener {
            Log.d("ProfileSettingsFragment", "boton perfil")
            val action = UserConfigFragmentDirections.actionUserConfigToProfileSettingsFragment()
            findNavController().navigate(action)
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCloseSession.setOnClickListener {
            findNavController().navigate(R.id.action_userConfig_to_userLogoutFragment)
        }
        binding.btnLanguage.setOnClickListener {
            val bottomSheet = LanguageBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, "languageBottomSheet")
        }
        binding.btnStorage.setOnClickListener {
            showAlertStorage()
        }
        binding.ivProfile.setOnClickListener {
            val action = UserConfigFragmentDirections.actionUserConfigToPhotoEditFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userConfig.collect { userProfile ->
                if (userProfile != null) {
                    binding.tvNameUser.text = userProfile.nick
                }
            }
        }
    }
    private fun setUpProfileBaseImage() {
        loadImageUri()
        if (binding.ivProfile.drawable == null) binding.ivProfile.setImageResource(R.drawable.usuario_1)
    }

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
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
            .setPositiveButton(getString(R.string.aceptDialog)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}
