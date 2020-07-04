package com.example.streetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.streetapp.fragments.GlobalTrainings
import com.example.streetapp.fragments.SettingsFragment
import com.example.streetapp.fragments.UserTrainings
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var fragmentTransaction: FragmentTransaction
    //private val navController by lazy { findNavController(this, R.id.fragment_container) }
   // private val appBarConfiguration by lazy { AppBarConfiguration(navController.graph) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        /*if (savedInstanceState == null) {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, UserTrainings())
            fragmentTransaction.commit()
        }*/
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.user_trainings -> {
                    Toast.makeText(this, "user trainings", Toast.LENGTH_SHORT).show()
                    val frag = UserTrainings.newInstance()
                    replaceFragment(frag, UserTrainings.TAG)
                    true
                }
                R.id.global_trainings -> {
                    Toast.makeText(this, "global trainings", Toast.LENGTH_SHORT).show()
                    val frag = GlobalTrainings.newInstance()
                    replaceFragment(frag, GlobalTrainings.TAG)
                    true
                }
                else -> false

            } }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)



       // val navController = findNavController(R.id.fragment_container)
       // setupActionBarWithNavController(navController)
        /*
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.user_trainings -> {
                    Toast.makeText(this, "user trainings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.global_trainings -> {
                    Toast.makeText(this, "global trainings", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false

            }
        }*/





    }

    override fun onSupportNavigateUp(): Boolean {
        Toast.makeText(this, "onsupport navigation", Toast.LENGTH_SHORT).show()
        return findNavController(R.id.fragment_container).navigateUp()
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag).addToBackStack("").commit()
    }

   /* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings -> {
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, SettingsFragment())
                fragmentTransaction.commit()
                return true
            }
            else -> {
                Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show()
                return false
            }
        }

    }*/
}