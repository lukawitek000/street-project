package com.example.streetapp.models

import android.os.Parcelable
import java.io.Serializable
import java.sql.Time

data class Exercise(var name: String,
                    var time: Int,
                    var numberOfRepetitions: Int,
                    var descritption: String,
                    var links: ArrayList<Link>) : Serializable {
    constructor() : this("", 0, 0, "", ArrayList<Link>())
}