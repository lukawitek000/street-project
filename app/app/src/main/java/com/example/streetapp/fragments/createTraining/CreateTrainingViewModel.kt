package com.example.streetapp.fragments.createTraining

import android.content.Context
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

class CreateTrainingViewModel(val context: Context) : ViewModel() {


    var trainingLinksCreating = ArrayList<Link>()

    var exercisesCreating: ArrayList<Exercise> = ArrayList()

    var training = Training()


    private val _exerciseLinks = MutableLiveData<ArrayList<Link>>()
    val exerciseLinks: LiveData<ArrayList<Link>>
        get() = _exerciseLinks

    var exercise: Exercise? = null

    private val database = AppDatabase.getDatabase(context)

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    init {
        _exerciseLinks.value = ArrayList()
        Log.i("CreateTrainingViewModel", "init view model createtraining")
    }


    var status = MutableLiveData<String>()

    fun insertNewTraining(training: Training){
        Log.i("CreateTrainingViewModel", "insert new Training outside uiScope $training")
        uiScope.launch {
            Log.i("CreateTrainingViewModel", "insert new Training $training")
            status.value = "loading"
            status.value = insertTraining(training)
            Log.i("CreateTrainingViewModel", "after inserting ")
        }
    }

    private suspend fun insertTraining(insertTraining: Training): String {
        return withContext(Dispatchers.IO){
            Log.i("CreateTrainingViewModel", "my training to insert $insertTraining")
            database.trainingDao().insertTrainingWithAllInfo(insertTraining)
            "end"
        }
    }

    fun deleteExercise(exercise: Exercise) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.trainingDao().deleteExercise(exercise)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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



    fun addExercise(newExercise: Exercise) {
        exercisesCreating.add(newExercise)
    }


    fun deleteAllExerciseLinks() {
        _exerciseLinks.value = ArrayList<Link>()
    }

    fun updateExercise(newExercise: Exercise) {
        val index = exercisesCreating.indexOf(exercise)

        exercisesCreating[index] = newExercise


    }

    fun deleteLink(link: Link){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.trainingDao().deleteLink(link)
            }
        }
    }


    fun updateTraining(training: Training){
        uiScope.launch {
            Log.i("CreateTrainingViewModel", "training to update $training")
            status.value = "loading"
            status.value = updateTrainingInDatabase(training)
        }
    }

    private suspend fun updateTrainingInDatabase(training: Training) : String {
        return withContext(Dispatchers.IO){
            database.trainingDao().updateWholeTraining(training)
            "updateFinished"
        }
    }


}