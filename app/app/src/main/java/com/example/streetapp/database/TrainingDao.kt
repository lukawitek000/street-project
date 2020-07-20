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

    @Delete
    abstract fun deleteExercise(exercises: Exercise)

    @Delete
    abstract fun deleteTraining(training: Training)

    @Delete
    abstract fun deleteLink(link: Link)

    @Update
    abstract fun updateTraining(training: Training)


    @Transaction
    @Query("SELECT * from training")
    abstract fun getAll(): List<TrainingWithExercisesAndLinks>


    @Transaction
    @Query("SELECT * from Exercise")
    abstract fun getAllExercises(): List<ExerciseWithLinks>


    @Query("SELECT * from link")
    abstract fun getAllLinks(): List<Link>





    fun getAllTrainings(): ArrayList<Training> {
        val trainings = ArrayList<Training>()

        val insertedData = getAll()
        val allLinks = getAllLinks()

        for (data in insertedData) {
            val trainingCreated = data.training

            trainingCreated.exercises = data.exercises as ArrayList<Exercise>
            trainingCreated.links = data.links as ArrayList<Link>

            for(exLinks in trainingCreated.exercises) {
                val links = allLinks.filter { link -> link.linksExerciseOwnerId == exLinks.exerciseId }
                exLinks.links.addAll(links)
            }


            trainings.add(trainingCreated)
            Log.i("UserTrainingsViewModel", "created training $trainingCreated")
        }

        return trainings
    }


    fun insertTrainingWithAllInfo(training: Training) {
        Log.i("inserting", "insert new training $training")
        val trainingLinks = training.links
        training.trainingId = insert(training)

        for( i in 0 until trainingLinks.size) {
            Log.i("inserting", "w forze $i linksOwner before ${trainingLinks[i].linksTrainingOwnerId}, traninid = ${training.trainingId}")
            trainingLinks[i].linksTrainingOwnerId = training.trainingId
        }

        for(exercise in training.exercises){
            for (link in exercise.links){
                link.linkId = 0
                link.linksExerciseOwnerId = 0
                link.linksTrainingOwnerId = 0
            }
        }

        val trainingExercises = training.exercises
        Log.i("inserting", "trainingExercises $trainingExercises")
        for (i in 0 until trainingExercises.size) {
            trainingExercises[i].parentTrainingId = training.trainingId
        }

        val exercisesIds = insertExercises(trainingExercises)
        Log.i("inserting", "exercisesIds $exercisesIds")

        for (i in 0 until trainingExercises.size) {
            for(j in 0 until trainingExercises[i].links.size) {
                trainingExercises[i].links[j].linksExerciseOwnerId = exercisesIds[i]
            }
            Log.i("inserting", "trainingExercises[$i].links ${trainingExercises[i].links}")
            insertLinksList(trainingExercises[i].links)
        }


        Log.i("inserting", "trainingLinks $trainingLinks")
        insertLinksList(trainingLinks)
        //insert(training)

    }



}