package com.example.streetapp.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streetapp.models.Training
import java.lang.IllegalArgumentException

class UserTrainingsViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserTrainingsViewModel::class.java)) {
            return UserTrainingsViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
