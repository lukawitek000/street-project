package com.example.streetapp.fragments

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.coroutines.*

class CreateTrainingViewModel(val activity: AppCompatActivity) : ViewModel() {


    var trainingLinksCreating = ArrayList<Link>()

    var exercisesCreating: ArrayList<Exercise> = ArrayList()

    var training = Training()


    private val _exerciseLinks = MutableLiveData<ArrayList<Link>>()
    val exerciseLinks: LiveData<ArrayList<Link>>
        get() = _exerciseLinks

    var exercise: Exercise? = null

    private val database = AppDatabase.getDatabase(activity)

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    init {
        _exerciseLinks.value = ArrayList()
    }


    fun insertNewTraining(training: Training){
        uiScope.launch {
            insertTraining(training)
        }
    }

    private suspend fun insertTraining(insertTraining: Training) {
        return withContext(Dispatchers.IO){
            Log.i("CreateTrainingViewModel", "my training to insert $insertTraining")
            //return@withContext
            database.trainingDao().insertTrainingWithAllInfo(insertTraining)
        }
    }


    fun populateExerciseLinks(links: ArrayList<Link>){
        _exerciseLinks.value = links
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


    fun deleteAllExerciseLinks() {
        _exerciseLinks.value = ArrayList<Link>()
    }

    fun updateExercise(newExercise: Exercise) {
        val index = exercisesCreating.indexOf(exercise)

        exercisesCreating[index] = newExercise


    }


}