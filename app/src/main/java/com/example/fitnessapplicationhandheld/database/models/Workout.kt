package com.example.fitnessapplicationhandheld.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WorkoutType{
    CARDIO,GYM
}

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey val timestamp: Long = 0L,
    val workoutType: WorkoutType = WorkoutType.GYM,
    val length: Long = 0,
    val label: String = "",
    val color: Int = 0,
    val distance: Int = 0,
    val averageSpeed : Double = 0.0,
    val calories: Int = 0,
)