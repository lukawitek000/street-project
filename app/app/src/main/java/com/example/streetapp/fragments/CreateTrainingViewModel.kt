package com.example.streetapp.fragments

import androidx.lifecycle.ViewModel
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link

class CreateTrainingViewModel : ViewModel() {
    var trainingLinksCreating = ArrayList<Link>()

    var exercisesCreating: ArrayList<Exercise> = ArrayList()

    val exerciseCreatingLinks: ArrayList<Link> = ArrayList()

    fun addLink(link: Link) {
        trainingLinksCreating.add(link)
    }


    fun addExercise(exercise: Exercise) {
        exercisesCreating.add(exercise)
    }

    fun addExerciseLink(link: Link) {
        exerciseCreatingLinks.add(link)
    }

    fun clearExerciseLinks() {
        exerciseCreatingLinks.clear()
    }




}