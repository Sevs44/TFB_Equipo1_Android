package com.saulhervas.easychat.utils


import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import java.util.*

object LocaleManager {

    fun setLocale(context: Context): Context {
        return updateResources(context, getLanguage(context))
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateResources(context, language)
    }

    fun getLanguage(context: Context): String {
        return SecurePreferences.getLanguage(context) ?: Locale.getDefault().language
    }

    private fun persistLanguage(context: Context, language: String) {
        SecurePreferences.saveLanguage(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res: Resources = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
