package com.saulhervas.easychat.domain.encryptedsharedpreference

import android.content.Context
import android.net.Uri
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecurePreferences {
    private const val PREF_FILE_NAME = "secure_prefs"
    private const val TOKEN_KEY = "biometric_token"
    private const val PROFILE_IMAGE = "profile_image"

    fun getEncryptedSharedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            PREF_FILE_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveBiometricToken(context: Context, token: String) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getBiometricToken(context: Context): String? {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }


    fun saveProfileImage(context: Context, imageUri: Uri) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString(PROFILE_IMAGE, imageUri.toString())
            apply()
        }
    }

    fun getProfileImage(context: Context): Uri? {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        val storedUriString = sharedPreferences.getString(PROFILE_IMAGE, null)
        return storedUriString?.let { Uri.parse(it) }
    }
}