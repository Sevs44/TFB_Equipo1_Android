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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentPhotoEditBinding
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class PhotoEditFragment : Fragment() {

    private lateinit var binding: FragmentPhotoEditBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imageUri: Uri

    companion object {
        private const val TAG = "PhotoEditFragment"
        private const val FILE_NAME = "profile_image.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        setupLaunchers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoEditBinding.inflate(inflater, container, false)
        setupUI(binding.root)
        loadSavedImage()
        setOnClickListener()
        return binding.root
    }

    private fun loadSavedImage() {
        val savedUri = SecurePreferences.getProfileImage(requireContext())
        savedUri?.let {
            binding.ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    private fun copyUriToInternalStorage(uri: Uri): Uri? {
        val file = File(requireContext().filesDir, FILE_NAME)
        return try {
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error copying URI to internal storage", e)
            null
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    copyUriToInternalStorage(uri)?.let {
                        SecurePreferences.saveProfileImage(requireContext(), it)
                        imageUri = it
                        binding.ivProfile.setImageURI(it)
                    }
                }
            }
        }

    private fun setupLaunchers() {
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d(TAG, "Picture taken successfully")
                    SecurePreferences.saveProfileImage(requireContext(), imageUri)
                    binding.ivProfile.setImageURI(imageUri)
                } else {
                    Log.d(TAG, "Failed to take picture")
                    showToast(R.string.failTakePicture)
                }
            }

        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d(TAG, "Camera permission granted")
                    openCamera()
                } else {
                    Log.d(TAG, "Camera permission denied")
                    showToast(R.string.permisionDenied)
                }
            }

        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d(TAG, "Gallery permission granted")
                    openGallery()
                } else {
                    Log.d(TAG, "Gallery permission denied")
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showPermissionDeniedDialog()
                    } else {
                        showToast(R.string.permisionDenied)
                    }
                }
            }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permisionDenied))
            .setMessage(getString(R.string.permissionIsNeeded))
            .setPositiveButton(getString(R.string.goToSettings)) { _, _ -> openAppSettings() }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun setOnClickListener() {
        binding.btnTakePhoto.setOnClickListener { checkPermissionsAndOpenCamera() }
        binding.btnDeletePhoto.setOnClickListener { deletePhoto() }
        binding.imBtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnSelectPhoto.setOnClickListener { checkPermissionsAndOpenGallery() }
    }

    private fun deletePhoto() {
        binding.ivProfile.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), R.mipmap.ic_pepe_round)
        )
        SecurePreferences.saveProfileImage(requireContext(), Uri.EMPTY)
    }

    private fun checkPermissionsAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()

            else -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        imageUri = createImageUri()
        SecurePreferences.saveProfileImage(requireContext(), imageUri)
        takePictureLauncher.launch(imageUri)
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "new_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: throw IllegalStateException("Failed to create image URI")
    }

    private fun checkPermissionsAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> openGallery()

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> requestGalleryPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            else -> requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun observeViewModel() {
        // Aquí puedes observar cambios en el ViewModel si es necesario
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
