package com.saulhervas.easychat.ui.photo_edit


import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class PhotoEditFragmentViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    val imageUri: MutableLiveData<Uri> = MutableLiveData()

    init {
        loadSavedImage()
    }

    private fun loadSavedImage() {
        val savedUri = SecurePreferences.getProfileImage(getApplication())
        savedUri?.let {
            imageUri.postValue(it)
        }
    }

    fun saveImageUri(uri: Uri) {
        SecurePreferences.saveProfileImage(getApplication(), uri)
        imageUri.postValue(uri)
    }

    fun deleteImage() {
        SecurePreferences.saveProfileImage(getApplication(), Uri.EMPTY)
        imageUri.postValue(Uri.EMPTY)
    }

    fun copyUriToInternalStorage(uri: Uri): Uri? {
        val fileName = "profile_image.jpg"
        val file = File(getApplication<Application>().filesDir, fileName)
        try {
            getApplication<Application>().contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return FileProvider.getUriForFile(
            getApplication(),
            "${getApplication<Application>().packageName}.provider",
            file
        )
    }

    fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "new_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val imageUri = getApplication<Application>().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        return imageUri ?: throw IllegalStateException("Failed to create image URI")
    }
}
