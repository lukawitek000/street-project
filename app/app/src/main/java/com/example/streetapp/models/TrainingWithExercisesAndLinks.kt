package com.example.streetapp.models

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingWithExercisesAndLinks(
    @Embedded val training: Training,
    @Relation(
        parentColumn = "trainingId",
        entityColumn = "parentTrainingId",
        entity = Exercise::class
    )
    val exercises: List<Exercise>,

    @Relation(
        parentColumn = "trainingId",
        entityColumn = "linksTrainingOwnerId",
        entity = Link::class
    )
    val links: List<Link>
)