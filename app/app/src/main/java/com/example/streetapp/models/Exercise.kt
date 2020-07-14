package com.example.streetapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.sql.Time

@Entity()
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long,
    var name: String,
    var time: Int,
    var numberOfRepetitions: Int,
    var description: String,
    @Relation(parentColumn = "exerciseId", entityColumn = "linkId", entity = Link::class)
                    var links: ArrayList<Link>) : Serializable {
    constructor() : this(1, "", 0, 0, "", ArrayList<Link>())
}