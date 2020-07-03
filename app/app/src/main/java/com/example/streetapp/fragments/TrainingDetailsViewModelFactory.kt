package com.example.streetapp.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streetapp.models.Training
import java.lang.IllegalArgumentException

class TrainingDetailsViewModelFactory(private val training: Training) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrainingDetailsViewModel::class.java)) {
            return TrainingDetailsViewModel(training) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}