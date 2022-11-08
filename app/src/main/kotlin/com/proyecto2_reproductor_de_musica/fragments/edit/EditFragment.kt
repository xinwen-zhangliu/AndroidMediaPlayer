package com.proyecto2_reproductor_de_musica.fragments.edit

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.proyecto2_reproductor_de_musica.R

class EditFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}