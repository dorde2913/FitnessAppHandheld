package com.example.fitnessapplicationhandheld.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hrlist")
data class HRList(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val parentID: Long = 0L,
    val value: Int = 0,
    val timeStamp: Long = 0L
)

