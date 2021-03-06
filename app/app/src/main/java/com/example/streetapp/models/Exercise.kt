package com.example.streetapp.models

import android.os.Parcelable
import androidx.room.*
import java.io.Serializable
import java.sql.Time

@Entity()
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var exerciseId: Long = 0L,
    var parentTrainingId: Long = 0L,
    var name: String,
    var time: Int,
    var numberOfRepetitions: Int,
    var description: String,
    @Ignore
    var links: ArrayList<Link>,
    var series: Int
    ) : Serializable {
    constructor() : this( name="", time = 0, numberOfRepetitions = 1, description = "", links=ArrayList<Link>(), series = 1)
}