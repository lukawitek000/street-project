package com.example.streetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.streetapp.fragments.UserTrainings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, UserTrainings())
        fragmentTransaction.commit()
    }
}