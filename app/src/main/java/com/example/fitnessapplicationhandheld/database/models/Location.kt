package com.example.fitnessapplicationhandheld.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val parentID: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timeStamp: Long = 0L
)