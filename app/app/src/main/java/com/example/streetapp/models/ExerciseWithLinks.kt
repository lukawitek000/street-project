package com.example.streetapp.models

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class ExerciseWithLinks(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "linksExerciseOwnerId",
        entity = Link::class
    )
    val links: List<Link>
)