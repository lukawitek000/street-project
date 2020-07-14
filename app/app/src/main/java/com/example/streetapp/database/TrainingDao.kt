package com.example.streetapp.database

import android.util.Log
import androidx.room.*
import com.example.streetapp.models.*

@Dao
abstract class TrainingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(training: Training) : Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLinksList(trainingLinks: List<Link>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertExercises(exercises: List<Exercise>) : List<Long>


    @Transaction
    @Query("SELECT * from training")
    abstract fun getAll(): List<TrainingWithExercisesAndLinks>


    @Transaction
    @Query("SELECT * from Exercise")
    abstract fun getAllExercises(): List<ExerciseWithLinks>


    @Query("SELECT * from link")
    abstract fun getAllLinks(): List<Link>


    fun insertTrainingWithAllInfo(training: Training) {
        val trainingLinks = training.links
        training.trainingId = insert(training)
        for( i in 0 until trainingLinks.size) {
            Log.i("UserTrainingsViewModel", "w forze $i linksOwner before ${trainingLinks[i].linksTrainingOwnerId}, traninid = ${training.trainingId}")
            trainingLinks[i].linksTrainingOwnerId = training.trainingId
        }

        val trainingExercises = training.exercises
        for (i in 0 until trainingExercises.size) {
            trainingExercises[i].parentTrainingId = training.trainingId
        }

        val exercisesIds = insertExercises(trainingExercises)

        for (i in 0 until trainingExercises.size) {
            for(j in 0 until trainingExercises[i].links.size) {
                trainingExercises[i].links[j].linksExerciseOwnerId = exercisesIds[i]
            }

            insertLinksList(trainingExercises[i].links)
        }


        Log.i("UserTrainingsViewModel", "trainingLinks $trainingLinks")
        insertLinksList(trainingLinks)
        //insert(training)

    }

}