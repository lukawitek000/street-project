package com.example.streetapp.models

import java.sql.Time

data class Exercise(var name: String,
                    var time: Time,
                    var numberOfRepetitions: Int,
                    var descritption: String,
                    var links: ArrayList<Link>)