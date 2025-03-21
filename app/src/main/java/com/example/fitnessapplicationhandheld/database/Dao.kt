package com.example.fitnessapplicationhandheld.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao{
    @Query("SELECT * FROM workout ORDER BY timestamp DESC")
    fun getWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE timestamp = :id")
    suspend fun getWorkout(id: Long): Workout?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)
    //znaci ne koristimo update, samo zamenimo ceo red

    @Query("SELECT * FROM workout WHERE label = :label ORDER BY timestamp DESC")
    fun getWorkoutsByLabel(label: String): Flow<List<Workout>>

    @Query("UPDATE workout SET calories = :value WHERE timestamp = :id")
    suspend fun updateCalories(id: Long, value: Int)

    @Query("UPDATE workout SET distance = :value WHERE timestamp = :id")
    suspend fun updateDistance(id: Long, value: Int)

    @Query("UPDATE workout SET length = :value WHERE timestamp = :id")
    suspend fun updateLength(id: Long, value: Long)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hrList: HRList)

    @Query("SELECT * FROM hrlist WHERE parentID = :id")
    fun getHRList(id: Long): Flow<List<HRList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (workoutLabel: WorkoutLabel)

    @Query("SELECT * FROM workoutLabel WHERE label = :label")
    suspend fun getWorkoutLabel(label: String): WorkoutLabel?


    @Query("SELECT AVG(value) FROM hrlist WHERE parentID = :id")
    fun getAverageBPM(id: Long): Flow<Int>


}