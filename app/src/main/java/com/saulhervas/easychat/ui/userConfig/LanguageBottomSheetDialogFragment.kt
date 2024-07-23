package com.saulhervas.easychat.ui.userConfig

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saulhervas.easychat.R
import com.saulhervas.easychat.utils.LocaleManager

class LanguageBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val languages = arrayOf("English", "Spanish", "French")
    private val languageCodes = arrayOf("en", "es", "fr")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_language_bottom_sheet_dialog, container, false)

        val listView: ListView = view.findViewById(R.id.lvLanguages)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languages)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            changeLanguage(languageCodes[position])
            dismiss()
        }

        return view
    }

    private fun changeLanguage(languageCode: String) {
        LocaleManager.setNewLocale(requireContext(), languageCode)
        activity?.recreate()  // Reinicia la actividad para aplicar el cambio de idioma
    }
}
