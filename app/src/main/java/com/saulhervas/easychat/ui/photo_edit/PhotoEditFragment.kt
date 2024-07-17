package com.saulhervas.easychat.ui.photo_edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentPhotoEditBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoEditFragment : Fragment() {

    private lateinit var binding: FragmentPhotoEditBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoEditBinding.inflate(inflater, container, false)
        setupUI(binding.root)
        setupCamera()
        setupGallery()
        setOnClickListener()
        return binding.root
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

    private fun setupCamera() {
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("PhotoEditFragment", "Picture taken successfully")
                    binding.ivProfile.setImageURI(imageUri)
                } else {
                    Log.d("PhotoEditFragment", "Failed to take picture")
                    Toast.makeText(requireContext(), "Failed to take picture", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d("PhotoEditFragment", "Camera permission granted")
                    openCamera()
                } else {
                    Log.d("PhotoEditFragment", "Camera permission denied")
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setupGallery() {
        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d("PhotoEditFragment", "Gallery permission granted")
                    openGallery()
                } else {
                    Log.d("PhotoEditFragment", "Gallery permission denied")
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showPermissionDeniedDialog()
                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Gallery permission is needed to select a photo. Please allow it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setOnClickListener() {
        binding.btnTakePhoto.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }
        binding.btnDeletePhoto.setOnClickListener {
            binding.ivProfile.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.pepe
                )
            )
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSelectPhoto.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                Log.d("PhotoEditFragment", "Requesting camera permission")
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        imageUri = createImageUri()
        SecurePreferences.saveProfileImage(requireContext(), imageUri.toString())
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
        return imageUri ?: throw IllegalStateException("Failed to create image URI")
    }

    private fun checkPermissionsAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> {
                Log.d("PhotoEditFragment", "Requesting gallery permission")
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    imageUri = uri
                    binding.ivProfile.setImageURI(uri)
                }
            }
        }

    private fun observeViewModel() {
        // Aquí puedes observar cambios en el ViewModel si es necesario
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        private const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}
