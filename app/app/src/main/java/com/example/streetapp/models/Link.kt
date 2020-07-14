package com.example.streetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Link(
    @PrimaryKey(autoGenerate = true) var linkId: Long = 0L,
    var linksTrainingOwnerId: Long = 0L,
    var linksExerciseOwnerId: Long = 0L,
    val title: String,
    val url: String)