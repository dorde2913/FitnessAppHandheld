package com.example.fitnessapplicationhandheld.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "workoutLabel")
data class WorkoutLabel(
    @PrimaryKey val label: String = "",
    val workoutType: WorkoutType = WorkoutType.GYM,
    val color: Int = 0
)