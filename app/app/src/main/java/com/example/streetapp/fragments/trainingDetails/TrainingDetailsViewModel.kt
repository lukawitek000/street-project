package com.example.streetapp.fragments.trainingDetails

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.streetapp.MainRepository
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.coroutines.*

class TrainingDetailsViewModel(var training: Training, private val context: Context) : ViewModel() {

    companion object {
        val TAG = TrainingDetailsViewModel::class.java.simpleName
    }


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    private val repository = MainRepository.getInstance(context)


    fun deleteTraining() {
        uiScope.launch {
            repository.deleteTraining(training)

        }
    }

    fun deleteExercise(exercise: Exercise) {
        uiScope.launch {
            repository.deleteExercise(exercise)
        }
    }

    fun deleteLink(link: Link) {
        uiScope.launch {
            repository.deleteLink(link)
        }
    }


}