package com.example.streetapp.fragments.doTraining

import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Training

class DoTrainingViewModel(val training: Training) : ViewModel() {


    var currentExerciseIndex: Int = 0
    var currentExercise: Exercise = training.exercises[currentExerciseIndex]





    fun nextExercise() {
        if(currentExerciseIndex < training.exercises.size - 1) {
            currentExercise = training.exercises[++currentExerciseIndex]
        }
    }

    fun isLastExercise(): Boolean {
        return currentExerciseIndex == training.exercises.size-1
    }









}