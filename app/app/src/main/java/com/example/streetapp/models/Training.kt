package com.example.streetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class Training(
 @PrimaryKey(autoGenerate = true) val id: Long,
                   var name: String,
                   var type: String,
                    var timeInMinutes: Int,
                    var description: String,
                    var creatingDate: Date,
 @Relation(parentColumn = "id", entityColumn = "linkId", entity = Link::class)
                    var links: ArrayList<Link>,
 @Relation(parentColumn = "id", entityColumn = "exerciseId", entity = Exercise::class)
                     val exercises: ArrayList<Exercise>
                    ) : Serializable {
 constructor() : this(1, "", "", 0, "", Date(), ArrayList<Link>(), ArrayList<Exercise>())
}