package com.example.streetapp.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training

class CreateTrainingViewModel : ViewModel() {


    var trainingLinksCreating = ArrayList<Link>()

    var exercisesCreating: ArrayList<Exercise> = ArrayList()

    var training = Training()

   // val exerciseCreatingLinks: ArrayList<Link> = ArrayList()

    private val _exerciseLinks = MutableLiveData<ArrayList<Link>>()
    val exerciseLinks: LiveData<ArrayList<Link>>
        get() = _exerciseLinks

    val exercise = Exercise()


    init {
        _exerciseLinks.value = ArrayList()
    }






    fun addExerciseLink(link: Link) {
        _exerciseLinks.value?.add(link)
        notifyChangeInLiveData()
    }

    fun deleteExerciseLink(link: Link) {
        _exerciseLinks.value?.remove(link)
        notifyChangeInLiveData()
    }


    private fun notifyChangeInLiveData() {
        _exerciseLinks.value = _exerciseLinks.value
    }


    fun addLink(link: Link) {
        trainingLinksCreating.add(link)
    }



    fun addExercise(exercise: Exercise) {
        exercisesCreating.add(exercise)
    }


    fun clearExerciseLinks() {
      //  exerciseCreatingLinks.clear()
    }

    fun deleteAllExerciseLinks() {
        _exerciseLinks.value = ArrayList<Link>()
    }


}