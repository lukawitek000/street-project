package com.example.streetapp.database

import android.util.Log
import androidx.room.*
import com.example.streetapp.models.*

@Dao
abstract class TrainingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTraining(training: Training) : Long


   // @Insert(onConflict = OnConflictStrategy.REPLACE)
   // abstract fun insertLinksList(trainingLinks: List<Link>) //LinkDao


    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //abstract fun insertExercisesList(exercises: List<Exercise>) : List<Long> //ExerciseDao

   // @Delete
   // abstract fun deleteExercise(exercises: Exercise) //ExerciseDao

    @Delete
    abstract fun deleteTraining(training: Training)

   // @Delete
   // abstract fun deleteLink(link: Link) //LinkDao


    @Transaction
    @Query("SELECT * from training")
    abstract fun getAll(): List<TrainingWithExercisesAndLinks>


   // @Transaction
  //  @Query("SELECT * from Exercise")
   // abstract fun getAllExercisesWithLinks(): List<ExerciseWithLinks> //ExerciseDao


    //@Query("SELECT * from link")
    //abstract fun getAllLinks(): List<Link> //LinkDao


    @Update
    abstract fun updateTraining(training: Training)


   // @Query("SELECT * FROM exercise WHERE :id == parentTrainingId")
   // abstract fun getExercisesOfTrainingByID(id: Long): List<Exercise> //ExerciseDao

 //   @Query("SELECT * FROM link WHERE :id == linksTrainingOwnerId")
  //  abstract fun getLinksOfTrainingByID(id: Long): List<Link> //LinkDao


   // @Query("SELECT * FROM link WHERE :id == linksExerciseOwnerId")
   // abstract fun getLinksOfExerciseByID(id:Long): List<Link> //LinkDao


   // @Update
   // abstract fun updateExercise(exercises: Exercise) //ExerciseDao

   // @Update
   // abstract fun updateLink(link: Link) //LinkDao


}