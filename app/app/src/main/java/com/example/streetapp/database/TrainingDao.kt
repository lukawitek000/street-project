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


    @Transaction
    @Query("SELECT * from training")
    abstract fun getAll(): List<TrainingWithExercisesAndLinks>


    @Transaction
    @Query("SELECT * from Exercise")
    abstract fun getAllExercises(): List<ExerciseWithLinks>


    @Query("SELECT * from link")
    abstract fun getAllLinks(): List<Link>


    @Update
    abstract fun updateTraining(training: Training)


    @Query("SELECT * FROM exercise WHERE :id == parentTrainingId")
    abstract fun getExercisesOfTrainingByID(id: Long): List<Exercise>

    @Query("SELECT * FROM link WHERE :id == linksTrainingOwnerId")
    abstract fun getLinksOfTrainingByID(id: Long): List<Link>


    @Query("SELECT * FROM link WHERE :id == linksExerciseOwnerId")
    abstract fun getLinksOfExerciseByID(id:Long): List<Link>


    @Update
    abstract fun updateExercise(exercises: Exercise)

    @Update
    abstract fun updateLink(link: Link)



    fun updateWholeTraining(training: Training){

        updateTraining(training)



        val oldExercises = getExercisesOfTrainingByID(training.trainingId)

        var trainingExercises = training.exercises

        for (oldExercise in oldExercises) {
            val exerciseToUpdate = training.exercises.filter { exercise -> exercise.exerciseId == oldExercise.exerciseId }



            if(exerciseToUpdate.isNotEmpty()) {
                updateExercise(exerciseToUpdate[0])
                Log.i("update", "exercise to update $exerciseToUpdate")
                trainingExercises =  ArrayList(trainingExercises.filter { exercise -> exercise.exerciseId != exerciseToUpdate[0].exerciseId })

                val exerciseLinks = ArrayList<Link>()
                for(link in exerciseToUpdate[0].links){
                    if (link.linksExerciseOwnerId == 0L){
                        link.linkId = 0
                        link.linksTrainingOwnerId = 0
                        link.linksExerciseOwnerId = exerciseToUpdate[0].exerciseId
                        exerciseLinks.add(link)
                    }
                }
                insertLinksList(exerciseLinks)



            }


        }

        if(trainingExercises.isNotEmpty()) {
            trainingExercises.forEach { exercise -> exercise.parentTrainingId = training.trainingId }
            val newExercisesIds = insertExercises(trainingExercises)

            for(i in 0 until trainingExercises.size){
                val newExerciseLinks = ArrayList<Link>()
                for (link in trainingExercises[i].links){
                    link.linkId = 0
                    link.linksTrainingOwnerId = 0
                    link.linksExerciseOwnerId = newExercisesIds[i]
                    newExerciseLinks.add(link)
                }
                insertLinksList(newExerciseLinks)
            }


            Log.i("update", "exercise to insert $trainingExercises")
        }


        val oldLinks = getLinksOfTrainingByID(training.trainingId)
        val trainingLinks = training.links
        val newTrainingLinks = ArrayList<Link>()
        for (link in trainingLinks) {
            if(!oldLinks.contains(link)){
                link.linkId = 0
                link.linksExerciseOwnerId = 0
                link.linksTrainingOwnerId = training.trainingId
                newTrainingLinks.add(link)
            }

        }

        for(oldLink in oldLinks) {
            if(!trainingLinks.contains(oldLink)){
               deleteLink(oldLink)
                Log.i("update", "Link to delete $oldLink")
            }
        }
        Log.i("update", "links to insert $newTrainingLinks")
        insertLinksList(newTrainingLinks)


        Log.i("update", "oldExercises $oldExercises")

        Log.i("update", "oldLinks training $oldLinks")

/*
        for(exercise in oldExercises) {
            val oldExerciseLinks: List<Link> = getLinksOfExerciseByID(exercise.exerciseId)
            Log.i("update", "oldExercises links $oldExerciseLinks")

            for (trainingExercise in training.exercises) {
                for(exerciseLink in trainingExercise.links)
                if(!oldExerciseLinks.contains(exerciseLink)){
                    exerciseLink.linksExerciseOwnerId = trainingExercise.exerciseId
                    Log.i("update", "exercise link to insert $exerciseLink")

                }



            }


        }*/





    }







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