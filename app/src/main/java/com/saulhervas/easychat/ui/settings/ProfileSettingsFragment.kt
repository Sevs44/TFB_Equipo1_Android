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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentProfileSettingsBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding
    private val viewModel: ProfileSettingViewModel by viewModels()
    private lateinit var token: String
    private var imageUri: Uri? = null

    companion object {
        private const val TAG = "ProfileSettingsFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getToken()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        setupUI(binding.root)
        setOnClickListener()
        setUpProfileBaseImage()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        savePreferences()
        viewModel.getUserProfile()
    }



    private fun setOnClickListener() = with(binding) {
        tvEditPhoto.setOnClickListener {
            navigateToPhotoEdit()
        }
        ivProfile.setOnClickListener {
            navigateToPhotoEdit()
        }
        imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToPhotoEdit() {
        findNavController().navigate(R.id.action_profileSettingsFragment_to_photoEditFragment)
    }
    private fun setUpProfileBaseImage() {
        loadImageUri()
        if (binding.ivProfile.drawable == null) binding.ivProfile.setImageResource(R.drawable.usuario_1)
    }

    private fun loadImageUri() {
        imageUri = SecurePreferences.getProfileImage(requireContext())
        imageUri?.let {
            binding.ivProfile.setImageURI(it)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userProfile.collect { userProfile ->
                if (userProfile != null) {
                    Log.d("UserProfile", "Nick: ${userProfile.nick}")
                    binding.tvNameProfile.text = userProfile.nick
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.keepSession.collect { isChecked ->
                binding.swSession.isChecked = isChecked
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.onlineStatus.collect { isChecked ->
                binding.swOnline.isChecked = isChecked
            }
        }
    }

    private fun savePreferences() {
        binding.swSession.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveKeepSessionPreference(isChecked)
            Log.d(TAG, "savePreferences: $isChecked")
        }
        binding.swOnline.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveShowOnlineStatusPreference(isChecked)
            Log.d(TAG, "savePreferences: $isChecked")
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
                setupUI(view.getChildAt(i))
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun getToken() {
        token = SecurePreferences.getBiometricToken(requireContext()).toString()
    }
}
