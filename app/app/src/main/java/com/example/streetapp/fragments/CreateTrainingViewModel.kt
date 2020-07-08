package com.example.streetapp.fragments

import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link

class CreateTrainingViewModel : ViewModel() {
    val trainingLinksCreating = ArrayList<Link>()

    val exercisesCreating: ArrayList<Exercise> = ArrayList()

    fun addLink(link: Link) {
        trainingLinksCreating.add(link)
    }


    fun addExercise(exercise: Exercise) {
        exercisesCreating.add(exercise)
    }



}