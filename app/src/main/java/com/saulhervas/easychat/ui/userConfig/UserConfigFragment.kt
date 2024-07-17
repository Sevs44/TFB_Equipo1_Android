package com.saulhervas.easychat.ui.userConfig

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>

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
        setupGalleryPermissionLauncher()
        if (isStoragePermissionGranted()) {
            //loadImageUri()
        } else {
            requestGalleryPermission()
        }

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
    }

    private fun observeViewModel() {
        // Aquí puedes observar cambios en el ViewModel si es necesario
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

    private fun loadImageUri() {
        SecurePreferences.getProfileImage(requireContext())?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupGalleryPermissionLauncher() {
        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d("UserConfigFragment", "Gallery permission granted")
                    loadImageUri()
                } else {
                    Log.d("UserConfigFragment", "Gallery permission denied")
                    // Aquí podrías manejar la denegación del permiso de manera adecuada
                }
            }
    }

    private fun requestGalleryPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            // Muestra un mensaje explicativo si el usuario ya ha negado el permiso anteriormente
            Log.d("UserConfigFragment", "Show rationale")
        } else {
            // Solicita el permiso directamente si es la primera vez o el usuario ha marcado "No volver a preguntar"
            requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
