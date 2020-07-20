package com.example.streetapp.fragments.userTrainings

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.streetapp.MainRepository
import com.example.streetapp.database.AppDatabase
import com.example.streetapp.models.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class UserTrainingsViewModel(val context: Context) : ViewModel() {

    private val _trainings = MutableLiveData<List<Training>>()
    val trainings: LiveData<List<Training>>
        get() = _trainings

    var allTrainings = ArrayList<Training>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val repository = MainRepository.getInstance(context)

    init {
        uiScope.launch {
          Log.i("UserTrainingsViewModel", "repository get all ${repository.getAll()}" )
        }
    }



    fun getAllData() {
        uiScope.launch {
            Log.i("UserTrainingsViewModel", "inside uiScope")
            _status.value = Status.LOADING
            try {
                allTrainings = repository.getAllTrainings()
                _status.value = Status.SUCCESS
            }catch (e: Exception){
                _status.value = Status.FAILURE
            }

            /*
            for (i in 0 until _allTrainings.value!!.size) {
                Log.i("UserTrainingsViewModel", "all trainings $i -> ${_allTrainings.value!![i]}")
            }

            Log.i("AllLinks", getAllLinks().toString())
            Log.i("AllLinks", _allTrainings.value.toString())
            Log.i("AllLinks", getAllExercises().toString())
            Log.i("AllLinks", getDataFromDatabase().toString())
            */

            //activity.deleteDatabase("street_workout_database")
        }
    }




    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun sortTrainings(position: Int) {
        Log.i("UserTrainingsViewModel", "sort trainings ${_trainings.value}")
        if (position == 0) {
            sortByLatest()
        } else {
            sortByName()
        }
    }

    private fun sortByName() {
        _trainings.value = _trainings.value?.sortedWith(compareBy {it.name})
    }

    private fun sortByLatest() {
        _trainings.value = _trainings.value?.sortedWith(compareByDescending{it.creatingDate})
    }


    fun filterTrainings(phrase: CharSequence, sortingPosition: Int) {
        val filterList = allTrainings.filter {
            if (it.name.contains(phrase, true) || it.type.contains(phrase, true)
                || it.description.contains(phrase, true)){
                return@filter true
            }
            false
        }
        _trainings.value = filterList
        sortTrainings(sortingPosition)
    }





/*
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
*/



}