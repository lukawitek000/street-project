package com.example.streetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.streetapp.fragments.GlobalTrainings
import com.example.streetapp.fragments.UserTrainings
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var fragmentTransaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, UserTrainings())
        fragmentTransaction.commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.user_trainings -> {
                    Toast.makeText(this, "user trainings", Toast.LENGTH_SHORT).show()
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, UserTrainings())
                    fragmentTransaction.commit()
                    true
                }
                R.id.global_trainings -> {
                    Toast.makeText(this, "global trainings", Toast.LENGTH_SHORT).show()
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragment_container, GlobalTrainings())
                    fragmentTransaction.commit()
                    true
                }
                else -> false

            } }
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
}