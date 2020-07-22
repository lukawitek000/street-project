package com.example.streetapp.fragments.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.settings)

        val languagePreference = findPreference<ListPreference>("language")
        languagePreference?.summary = languagePreference?.value.toString()

        languagePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            preference?.summary = newValue.toString()

            (activity as MainActivity).updateLanguage(newValue.toString())

            findNavController().navigateUp()
            findNavController().navigate(R.id.action_user_trainings_to_settingsFragment)
            true
        }


        return super.onCreateView(inflater, container, savedInstanceState)
    }

}