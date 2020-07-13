package com.example.streetapp.fragments

import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Training

class UserTrainingsViewModel : ViewModel() {
    var trainings = ArrayList<Training>()
}