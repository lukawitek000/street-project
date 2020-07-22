package com.example.streetapp

import android.content.Context
import android.util.Log
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import com.example.streetapp.models.TrainingWithExercisesAndLinks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {

    suspend fun deleteTraining(training: Training){
        return withContext(Dispatchers.IO){
            database.trainingDao().deleteTraining(training)
        }
    }

    suspend fun deleteLink(link: Link){
        return withContext(Dispatchers.IO){
            database.linkDao().deleteLink(link)
        }
    }


    suspend fun deleteExercise(exercise: Exercise){
        return withContext(Dispatchers.IO){
            database.exerciseDao().deleteExercise(exercise)
        }
    }




    suspend fun getAll(): List<TrainingWithExercisesAndLinks> {
        return withContext(Dispatchers.IO){
            database.trainingDao().getAll()
        }
    }

    suspend fun getAllTrainings(): ArrayList<Training>{
        return withContext(Dispatchers.IO){
            val trainings = ArrayList<Training>()
            val insertedData = database.trainingDao().getAll()
            val allLinks = database.linkDao().getAllLinks()
            for (data in insertedData) {
                val trainingCreated = data.training
                trainingCreated.exercises = data.exercises as ArrayList<Exercise>
                trainingCreated.links = data.links as ArrayList<Link>
                for(exerciseLinks in trainingCreated.exercises) {
                    val links = allLinks.filter { link -> link.linksExerciseOwnerId == exerciseLinks.exerciseId }
                    exerciseLinks.links.addAll(links)
                }
                trainings.add(trainingCreated)
                Log.i("UserTrainingsViewModel", "created training $trainingCreated")
            }
            return@withContext trainings
        }
    }



    suspend fun insertTrainingWithAllInfo(training: Training): Status{
        return withContext(Dispatchers.IO){
            training.trainingId = database.trainingDao().insertTraining(training)
            insertTrainingLinks(training)
            val exercisesIds = insertListOfExercises(training)
            insertLinksOfExercises(training, exercisesIds)
            Status.INSERTED
        }
    }

    private fun insertLinksOfExercises(training: Training, exercisesIds: List<Long>) {
        for(exercise in training.exercises){
            for (link in exercise.links){
                link.linkId = 0
                link.linksExerciseOwnerId = 0
                link.linksTrainingOwnerId = 0
            }
        }

        for (i in 0 until training.exercises.size) {
            for(j in 0 until training.exercises[i].links.size) {
                training.exercises[i].links[j].linksExerciseOwnerId = exercisesIds[i]
            }
            Log.i("inserting", "trainingExercises[$i].links ${training.exercises[i].links}")
            database.linkDao().insertLinksList(training.exercises[i].links)
        }
    }

    private fun insertListOfExercises(training: Training): List<Long> {
        Log.i("inserting", "trainingExercises $training.exercises")
        for (i in 0 until training.exercises.size) {
            training.exercises[i].parentTrainingId = training.trainingId
        }

        val exercisesIds = database.exerciseDao().insertExercisesList(training.exercises)
        Log.i("inserting", "exercisesIds $exercisesIds")
        return exercisesIds
    }

    private fun insertTrainingLinks(training: Training) {
        val trainingLinks = training.links
        for(link in trainingLinks){
            link.linksTrainingOwnerId = training.trainingId
        }
        Log.i("inserting", "trainingLinks $trainingLinks")
        database.linkDao().insertLinksList(trainingLinks)
    }



    suspend fun updateTraining(training: Training) : Status {
        return withContext(Dispatchers.IO){
            database.trainingDao().updateTraining(training)
            updateTrainingLinks(training)
            updateTrainingsExercises(training)
            Status.UPDATED
        }
    }

    private fun updateTrainingsExercises(training: Training){
        val oldExercises = database.exerciseDao().getExercisesOfTrainingByID(training.trainingId)

        var trainingExercises = training.exercises

        for (oldExercise in oldExercises) {
            val exerciseToUpdate = training.exercises.filter { exercise -> exercise.exerciseId == oldExercise.exerciseId }

            if(exerciseToUpdate.isNotEmpty()) {
                database.exerciseDao().updateExercise(exerciseToUpdate[0])
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
                database.linkDao().insertLinksList(exerciseLinks)
            }
        }

        if(trainingExercises.isNotEmpty()) {
            trainingExercises.forEach { exercise -> exercise.parentTrainingId = training.trainingId }
            val newExercisesIds = database.exerciseDao().insertExercisesList(trainingExercises)

            for(i in 0 until trainingExercises.size){
                val newExerciseLinks = ArrayList<Link>()
                for (link in trainingExercises[i].links){
                    link.linkId = 0
                    link.linksTrainingOwnerId = 0
                    link.linksExerciseOwnerId = newExercisesIds[i]
                    newExerciseLinks.add(link)
                }
                database.linkDao().insertLinksList(newExerciseLinks)
            }

            Log.i("update", "exercise to insert $trainingExercises")
        }
    }

    private fun updateTrainingLinks(training: Training) {
        val oldLinks = database.linkDao().getLinksOfTrainingByID(training.trainingId)
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
                database.linkDao().deleteLink(oldLink)
                Log.i("update", "Link to delete $oldLink")
            }
        }
        Log.i("update", "links to insert $newTrainingLinks")
        database.linkDao().insertLinksList(newTrainingLinks)

    }


    companion object {
        private lateinit var database: AppDatabase
        private var INSTANCE: MainRepository? = null

        fun getInstance(context: Context) : MainRepository {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null) {
                    instance = MainRepository()
                    database = AppDatabase.getDatabase(context)
                }
                return instance
            }
        }
    }

}