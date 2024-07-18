package com.saulhervas.easychat.ui.settings

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentProfileSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding
    private val viewModel: ProfileSettingViewModel by viewModels()
    private val args: ProfileSettingsFragmentArgs by navArgs()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        setOnClickListener()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        observeViewModel()
        savePreferences()
    }



    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.getUserProfile(token)
            viewModel.userProfile.collect {
                if (it != null) {
                    Log.d("ProfileSettingsFragment", "observeViewModel: it ${it.nick}")
                    binding.etNameProfile.setText(it.nick)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.keepSession.collect { isChecked ->
                        binding.swSession.isChecked = isChecked
                    }
                }
                launch {
                    viewModel.onlineStatus.collect { isChecked ->
                        binding.swOnline.isChecked = isChecked
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            viewModel.getUserProfile(token)
        }
    }

    private fun getUserArgs() {
        token = args.token
    }

    fun savePreferences() {
        binding.swSession.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveKeepSessionPreference(isChecked)
            Log.d("ProfileSettingsFragment", "savePreferences: $isChecked")
        }
        binding.swOnline.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveShowOnlineStatusPreference(isChecked)
            Log.d("ProfileSettingsFragment", "savePreferences: $isChecked")
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

}
