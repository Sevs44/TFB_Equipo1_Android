package com.saulhervas.easychat.ui.settings

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.databinding.FragmentProfileSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        Log.d("ProfileSettingsFragment", "View created")
        setupCamera()
        setOnClickListener()

        setupUI(binding.root)
        return binding.root
    }

    private fun setupCamera() {
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("ProfileSettingsFragment", "Picture taken successfully")
                    binding.ivProfile.setImageURI(imageUri)
                } else {
                    Log.d("ProfileSettingsFragment", "Failed to take picture")
                    Toast.makeText(requireContext(), "Failed to take picture", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d("ProfileSettingsFragment", "Camera permission granted")
                    openCamera()
                } else {
                    Log.d("ProfileSettingsFragment", "Camera permission denied")
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setOnClickListener() {
        binding.tvEditPhoto.setOnClickListener {
            Log.d("ProfileSettingsFragment", "tvEditPhoto clicked")
            checkPermissionsAndOpenCamera()
        }
        binding.ivProfile.setOnClickListener {
            Log.d("ProfileSettingsFragment", "ivProfile clicked")
            checkPermissionsAndOpenCamera()
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            else -> {
                Log.d("ProfileSettingsFragment", "Requesting camera permission")
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        imageUri = createImageUri()
        takePictureLauncher.launch(imageUri)
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "new_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val imageUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        return imageUri ?: throw IllegalStateException("No se pudo crear URI para la imagen")
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
