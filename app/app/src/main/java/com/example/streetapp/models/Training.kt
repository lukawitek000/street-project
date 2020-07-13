package com.example.streetapp.models

import java.io.Serializable
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

data class Training(var name: String,
                   var type: String,
                    var timeInMinutes: Int,
                    var description: String,
                    var creatingDate: Date,
                    var links: ArrayList<Link>,
                    var exercises: ArrayList<Exercise>
                    ) : Serializable {
 constructor() : this("", "", 0, "", Date(), ArrayList<Link>(), ArrayList<Exercise>())
}