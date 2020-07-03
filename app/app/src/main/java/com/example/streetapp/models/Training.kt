package com.example.streetapp.models

import java.io.Serializable
import java.sql.Time
import java.util.*

data class Training(var name: String,
                   var type: String,
                    var timeInMinutes: Int,
                    var description: String,
                    var creatingDate: Date,
                    var links: Array<String>
                    //var exercises: Array<Exercise>
                    ) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Training

        if (name != other.name) return false
        if (type != other.type) return false
        if (timeInMinutes != other.timeInMinutes) return false
        if (description != other.description) return false
        if (creatingDate != other.creatingDate) return false
        if (!links.contentEquals(other.links)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + timeInMinutes.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creatingDate.hashCode()
        result = 31 * result + links.contentHashCode()
        return result
    }
}