package com.example.streetapp

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.streetapp.models.Link
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.visibility = View.GONE

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)

       // supportActionBar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
       // supportActionBar?.setCustomView(R.layout.custom_toolbar)
        //supportActionBar?.title = "Street Workout Training App"
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val language = preferences.getString("language", "en")
        if (language != null) {
            updateLanguage(language)
        }
    }

    fun updateLanguage(selectedLanguage: String){


        val selectedLanguageCode = if(selectedLanguage == "Polski"){
            "pl"
        }else {
            "en"
        }
        val locale = Locale(selectedLanguageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, null)
    }


    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
        return findNavController(R.id.fragment_container).navigateUp()
    }


    fun openExternalLink(link: Link) {
        val intent = Intent(Intent.ACTION_VIEW)
        var uriAddress = link.url
        if (!uriAddress.startsWith("http://") && !uriAddress.startsWith("https://")){
            uriAddress = "http://$uriAddress"
        }
        intent.data = Uri.parse(uriAddress)
        startActivity(intent)
    }

}