package com.example.streetapp.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streetapp.fragments.userTrainings.UserTrainingsViewModel
import com.example.streetapp.models.Training
import java.lang.IllegalArgumentException

class DoTrainingViewModelFactory(private val training: Training) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DoTrainingViewModel::class.java)) {
            return DoTrainingViewModel(training) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}