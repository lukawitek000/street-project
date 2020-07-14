package com.example.streetapp.fragments

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class UserTrainingsViewModel(val activity: AppCompatActivity) : ViewModel() {
    var trainings = ArrayList<Training>()

    private val _allTrainings = MutableLiveData<ArrayList<Training>>()

    val allTrainings: LiveData<ArrayList<Training>>
        get() = _allTrainings

    private val _justForObservation = MutableLiveData<Int>()

    val justForObservation : LiveData<Int>
     get() = _justForObservation


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val database = AppDatabase.getDatabase(activity)


    init {
        _allTrainings.value = ArrayList()
        Log.i("UserTrainingsViewModel", "database test $database")

        uiScope.launch {

            _allTrainings.value = getAllTrainings()
            //trainings = getAllTrainings()
            _justForObservation.value = 0

        }


        //Log.i("UserTrainingsViewModel", "database test get all ${database.trainingDao().getAll()}")
    }

    private suspend fun getAllTrainings(): ArrayList<Training> {
        return withContext(Dispatchers.IO) {
            database.trainingDao().getAllTrainings()
        }

    }


    private suspend fun getAllExercises(): List<ExerciseWithLinks> {
        return withContext(Dispatchers.IO){
            database.trainingDao().getAllExercises()
        }

    }

    private suspend fun getAllLinks(): List<Link> {
        return withContext(Dispatchers.IO) {
            database.trainingDao().getAllLinks()
        }

    }

    private suspend fun insertFakeData(index: Int) {
        return withContext(Dispatchers.IO) {
            val insertTraining = Training(name="Some name $index", type = "Handstand",timeInMinutes =  11, description = "descritpion super hiper",
                creatingDate = Date(2002 -1900, 10, 11),
               links =  arrayListOf(Link( title = "some link 12345 $index", url = "https://www.youtube.com/watch?v=l59bF4h1BXQ")),
              exercises =   arrayListOf(
                    Exercise( name = "exervise name $index", time = 12, numberOfRepetitions = 12, description = "some descritpion",
                        links = arrayListOf(Link( title = "some link exercise $index",url =  "urlexercise.com"))),
                    Exercise(name = "exervise name 20000000 $index", time =  10, numberOfRepetitions = 12, description = "some descritpion",
                        links = arrayListOf(
                            Link(title =  "some link exercise 20000000 $index", url = "https://kurawear.pl/produkt/koszulka-street-workout-v2/")
            ))))
            database.trainingDao().insertTrainingWithAllInfo(training = insertTraining)
        }
    }

    private suspend fun getDataFromDatabase(): List<TrainingWithExercisesAndLinks> {
        return withContext(Dispatchers.IO){
            database.trainingDao().getAll()
        }
    }
}