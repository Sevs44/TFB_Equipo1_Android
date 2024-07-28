package com.saulhervas.easychat.ui.photo_edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentPhotoEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoEditFragment : Fragment() {

    private lateinit var binding: FragmentPhotoEditBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val viewModel: PhotoEditFragmentViewModel by viewModels()

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
        setOnClickListener()
        setupCamera()
        setupGallery()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.imageUri.observe(this) { uri ->
            if (uri != Uri.EMPTY) {
                binding.ivProfile.setImageURI(uri)
            } else {
                binding.ivProfile.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.usuario_1
                    )
                )
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnTakePhoto.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }
        binding.btnDeletePhoto.setOnClickListener {
            viewModel.deleteImage()
        }
        binding.imBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSelectPhoto.setOnClickListener {
            checkPermissionsAndOpenGallery()
        }
    }

    private fun setupCamera() {
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    viewModel.saveImageUri(viewModel.imageUri.value!!)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.FailedPhoto),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        requestCameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permisionDenied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setupResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    data?.data?.let { uri ->
                        val copiedUri = viewModel.copyUriToInternalStorage(uri)
                        copiedUri?.let {
                            viewModel.saveImageUri(it)
                        }
                    }
                }
            }
    }

    private fun setupGallery() {
        requestGalleryPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openGallery()
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showPermissionDeniedDialog()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.permissionIsNeeded),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        setupResultLauncher()
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permissionIsNeeded))
            .setMessage(getString(R.string.messageGalleryPermision))
            .setPositiveButton(getString(R.string.goToSettings)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
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
        val uri = viewModel.createImageUri()
        viewModel.saveImageUri(uri)
        takePictureLauncher.launch(uri)
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
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
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
