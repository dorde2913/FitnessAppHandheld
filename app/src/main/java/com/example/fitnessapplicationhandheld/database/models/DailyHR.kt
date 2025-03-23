package com.example.fitnessapplicationhandheld.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dailyHR")
data class DailyHR(
    @PrimaryKey(autoGenerate = false) val hour: Int = 0,
    val value: Int = 0
)
