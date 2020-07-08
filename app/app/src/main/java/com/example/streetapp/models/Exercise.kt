package com.example.streetapp.models

import java.sql.Time

data class Exercise(var name: String,
                    var time: Int,
                    var numberOfRepetitions: Int,
                    var descritption: String,
                    var links: ArrayList<Link>)