package com.example.streetapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Link(
    @PrimaryKey(autoGenerate = true) val linkId: Long,
    val title: String,
    val url: String)