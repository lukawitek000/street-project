package com.example.streetapp.models

import java.sql.Time
import java.util.*

data class Training(var name: String,
                    var type: String,
                    var time: Time,
                    var description: String,
                    var links: Array<String>,
                    var exercises: Array<Exercise>
                    )