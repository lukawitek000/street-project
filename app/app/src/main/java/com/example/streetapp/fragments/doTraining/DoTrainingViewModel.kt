package com.example.streetapp.fragments.doTraining

import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Training

class DoTrainingViewModel(val training: Training) : ViewModel() {


    var currentExerciseIndex: Int = 0
    var currentExercise: Exercise = training.exercises[currentExerciseIndex]

    var setsLeft = currentExercise.series

    var setsOfEachExerciseLeft = getSetsOfEachExercise()

    private fun getSetsOfEachExercise(): ArrayList<Int> {
        val arr = arrayListOf<Int>()
        for(exercise in training.exercises){
            arr.add(exercise.series)
        }
        return arr
    }

    fun nextExerciseAndChangeSetsLeft(){
        if(currentExerciseIndex < training.exercises.size){
            setsOfEachExerciseLeft[currentExerciseIndex]--
        }else{
            currentExerciseIndex = 0
        }

        if(currentExerciseIndex < training.exercises.size - 1) {
            currentExercise = training.exercises[++currentExerciseIndex]
            if(setsOfEachExerciseLeft[currentExerciseIndex] <= 0){
                nextExerciseAndChangeSetsLeft()
            }
        }
    }

    fun getCurrentSetsLeft(): Int{
        return setsOfEachExerciseLeft[currentExerciseIndex]
    }

    fun firstExercise(){
        if(currentExerciseIndex < training.exercises.size){
            setsOfEachExerciseLeft[currentExerciseIndex]--
        }
        currentExerciseIndex = 0
        currentExercise = training.exercises[currentExerciseIndex]
        if(setsOfEachExerciseLeft[currentExerciseIndex] <= 0){
            nextExerciseAndChangeSetsLeft()
        }
    }
    fun isLastExerciseInSet(): Boolean {
        Log.i("DoTrainingViewModel", "os Last exercise in set")
        return (currentExerciseIndex == training.exercises.size-1) || isLastExerciseWithSetsLeft()
    }

    private fun isLastExerciseWithSetsLeft(): Boolean {
        Log.i("DoTrainingViewModel", "index = $currentExerciseIndex , set = $setsOfEachExerciseLeft")
        for( i in (currentExerciseIndex+1) until (setsOfEachExerciseLeft.size)){
            if(setsOfEachExerciseLeft[i] > 0){
                return false
            }
        }
        return true
    }

    fun isTheEndOfTheTraining(): Boolean {
        if(setsOfEachExerciseLeft.all {
                it <= 1
            }){
            if(setsOfEachExerciseLeft.count{
                    it == 1
                } == 1){
                return true
            }
        }
        return false

    }


    fun nextExercise() {
        if(currentExerciseIndex < training.exercises.size - 1) {
            currentExercise = training.exercises[++currentExerciseIndex]
            setsLeft = currentExercise.series
        }
    }

    fun isLastExercise(): Boolean {
        return currentExerciseIndex == training.exercises.size-1
    }


    fun nextSet() {
        setsLeft--
    }

    fun isLastSet(): Boolean {
        return setsLeft <= 1
    }






}