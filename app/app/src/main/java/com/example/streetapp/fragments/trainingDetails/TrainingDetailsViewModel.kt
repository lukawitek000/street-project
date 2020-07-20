package com.example.streetapp.fragments.trainingDetails

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.coroutines.*

class TrainingDetailsViewModel(var training: Training, private val context: Context) : ViewModel() {

    companion object {
        val TAG = TrainingDetailsViewModel::class.java.simpleName
    }


    private val database = AppDatabase.getDatabase(context)


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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