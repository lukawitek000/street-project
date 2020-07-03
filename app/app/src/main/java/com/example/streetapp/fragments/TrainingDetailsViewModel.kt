package com.example.streetapp.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Training

class TrainingDetailsViewModel(training: Training) : ViewModel() {

    companion object {
        val TAG = TrainingDetailsViewModel::class.java.simpleName
    }


    init {
        Log.i(TAG, "view model training details")
    }
}