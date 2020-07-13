package com.example.streetapp

import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

object TemporaryDatabase {
    private val trainings: ArrayList<Training> = populate()

    private fun populate() : ArrayList<Training>{
        val trainingsList = ArrayList<Training>()
        for(i in 0..50) {
            var random = (0..255).random().toString()
            var day = (1..30).random()
            var month = (1..12).random()
            var year = Random.nextInt(2010, 2020)
            var time = Random.nextInt(10, 200)
            trainingsList.add(Training("name $i", "Handstand", time, "random $random",
                Date(year -1900, month, day), arrayListOf(Link("some link $random", "url.com")),
                arrayListOf(
                    Exercise("exervise name", 12, 12, "some descritpion",
                    arrayListOf(Link("some link exercise", "urlexercise.com"))),
                    Exercise("exervise name 20000000", 10, 12, "some descritpion",
                        arrayListOf(Link("some link exercise", "urlexercise.com")))
                )))
        }
        return trainingsList
    }

    fun updateTraining(training: Training, update: Training) {
        val index = trainings.indexOf(training)
        trainings[index] = update
    }



    fun insert(training: Training){
        trainings.add(training)
    }

    fun getAll() : ArrayList<Training>{
        return trainings
    }


    fun deleteTraining(training: Training) {
        trainings.remove(training)
    }

}