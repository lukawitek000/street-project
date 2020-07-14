package com.example.streetapp.fragments

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Training

class UserTrainingsViewModel(val activity: AppCompatActivity) : ViewModel() {
    var trainings = ArrayList<Training>()
    var allTrainings = ArrayList<Training>()



    init {
        var database = AppDatabase.getDatabase(activity)
        Log.i("UserTrainingsViewModel", "database test $database")
    }
}