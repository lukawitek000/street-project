package com.example.streetapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.streetapp.models.Training

@Dao
interface TrainingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(training: Training)


    @Query("SELECT * from training")
    fun getALL(): ArrayList<Training>



}