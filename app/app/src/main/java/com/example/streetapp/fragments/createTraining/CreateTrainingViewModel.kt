package com.example.streetapp.fragments.createTraining

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetapp.MainRepository
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.coroutines.*
import java.lang.Exception

class CreateTrainingViewModel(val context: Context) : ViewModel() {


    var trainingLinksCreating = ArrayList<Link>()

    var exercisesCreating: ArrayList<Exercise> = ArrayList()

    var training = Training()


    private val _exerciseLinks = MutableLiveData<ArrayList<Link>>()
    val exerciseLinks: LiveData<ArrayList<Link>>
        get() = _exerciseLinks

    var exercise: Exercise? = null



    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    enum class Status{
        LOADING, INSERTED, UPDATED, FAILURE
    }


    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val repository = MainRepository.getInstance(context)

    init {
        _exerciseLinks.value = ArrayList()
    }



    fun insertNewTraining(training: Training){
        uiScope.launch {
            try {
                _status.value = Status.LOADING
                _status.value = repository.insertTrainingWithAllInfo(training)
            }catch (e: Exception){
                _status.value = Status.FAILURE
            }
        }
    }



    fun deleteExercise(exercise: Exercise) {
        uiScope.launch {
            repository.deleteExercise(exercise)
        }
    }


    fun populateExerciseLinks(links: ArrayList<Link>){
        _exerciseLinks.value = links
    }
    // use in create exercise


    fun addExerciseLink(link: Link) {
        _exerciseLinks.value?.add(link)
        notifyChangeInExerciseLinksLiveData()
    }

    fun deleteExerciseLink(link: Link) {
        _exerciseLinks.value?.remove(link)
        notifyChangeInExerciseLinksLiveData()
    }


    private fun notifyChangeInExerciseLinksLiveData() {
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
            repository.deleteLink(link)
        }
    }


    fun updateTraining(training: Training){
        uiScope.launch {
            _status.value = Status.LOADING
            _status.value = repository.updateTraining(training)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}