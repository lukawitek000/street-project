package com.example.streetapp.database


import androidx.room.*
import com.example.streetapp.models.*

@Dao
interface TrainingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTraining(training: Training) : Long

    @Delete
    fun deleteTraining(training: Training)

    @Transaction
    @Query("SELECT * from training")
    fun getAll(): List<TrainingWithExercisesAndLinks>

    @Update
    fun updateTraining(training: Training)

}