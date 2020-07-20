package com.example.streetapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class Training(
    @PrimaryKey(autoGenerate = true)
    var trainingId: Long = 0L,
    var name: String,
    var type: String,
    var timeInMinutes: Int,
    var description: String,
    var creatingDate: Date,
    @Ignore
    var links: ArrayList<Link>,
    @Ignore
    var exercises: ArrayList<Exercise>) : Serializable {
 constructor() : this(0, "", "", 0, "", Date(), ArrayList<Link>(), ArrayList<Exercise>())
}