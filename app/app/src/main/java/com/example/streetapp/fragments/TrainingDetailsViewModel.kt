package com.example.streetapp.fragments

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.coroutines.*

class TrainingDetailsViewModel(var training: Training, private val activity: AppCompatActivity) : ViewModel() {

    companion object {
        val TAG = TrainingDetailsViewModel::class.java.simpleName
    }


    private val database = AppDatabase.getDatabase(activity)



    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    init {
        Log.i(TAG, "view model training details")
    }


    fun deleteTraining() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.trainingDao().deleteTraining(training)
            }

        }
    }

    fun deleteExercise(exercise: Exercise) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.trainingDao().deleteExercise(exercise)
            }
        }
    }

    fun deleteLink(link: Link) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.trainingDao().deleteLink(link)
            }
        }
    }


}