package com.example.streetapp.database

import androidx.room.*
import com.example.streetapp.models.Link

@Dao
interface LinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLinksList(links: List<Link>)

    @Delete
    fun deleteLink(link: Link)

    @Query("SELECT * from link")
    fun getAllLinks(): List<Link>

    @Update
    fun updateLink(link: Link)


    @Query("SELECT * FROM link WHERE :id == linksTrainingOwnerId")
    fun getLinksOfTrainingByID(id: Long): List<Link>

    @Query("SELECT * FROM link WHERE :id == linksExerciseOwnerId")
    fun getLinksOfExerciseByID(id:Long): List<Link>
}