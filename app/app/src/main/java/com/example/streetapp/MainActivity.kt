package com.example.streetapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.streetapp.models.Link
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)
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