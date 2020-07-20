package com.example.streetapp.database

import androidx.room.*
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.ExerciseWithLinks

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercisesList(exercises: List<Exercise>) : List<Long>

    @Delete
    fun deleteExercise(exercises: Exercise)

    @Update
    fun updateExercise(exercises: Exercise)


    @Transaction
    @Query("SELECT * from Exercise")
    fun getAllExercisesWithLinks(): List<ExerciseWithLinks>

    @Query("SELECT * FROM exercise WHERE :id == parentTrainingId")
    fun getExercisesOfTrainingByID(id: Long): List<Exercise>
}