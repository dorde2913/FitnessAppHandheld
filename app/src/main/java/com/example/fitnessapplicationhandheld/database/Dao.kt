package com.example.fitnessapplicationhandheld.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitnessapplicationhandheld.database.models.DailyHR
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Location
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao{
    @Query("SELECT * FROM workout ORDER BY timestamp DESC")
    fun getWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE timestamp = :id")
    suspend fun getWorkout(id: Long): Workout?

    @Query("SELECT * FROM workout WHERE workoutType = :type ORDER BY timestamp DESC")
    fun getWorkoutsByType(type: WorkoutType): Flow<List<Workout>>

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

    @Query("SELECT * FROM workout WHERE timestamp = :id")
    fun getWorkoutFlow(id: Long): Flow<Workout>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(hrList: HRList)

    @Query("SELECT * FROM hrlist WHERE parentID = :id ORDER BY timeStamp ASC")
    fun getHRList(id: Long): Flow<List<HRList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (workoutLabel: WorkoutLabel)

    @Query("SELECT * FROM workoutLabel WHERE label = :label")
    suspend fun getWorkoutLabel(label: String): WorkoutLabel?


    @Query("SELECT AVG(value) FROM hrlist WHERE parentID = :id")
    fun getAverageBPM(id: Long): Flow<Int>

    @Query("UPDATE workout SET averageSpeed = :speed WHERE timestamp = :workoutId")
    suspend fun updateSpeed(workoutId: Long, speed: Double)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (dailyHR: DailyHR)

    @Query("UPDATE dailyHR SET value = :value WHERE hour = :hour")
    suspend fun updateDailyHR(value: Int, hour: Int)

    @Query("SELECT * FROM dailyHR ORDER BY hour ASC")
    fun getDailyHR(): Flow<List<DailyHR>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (location: Location)


    @Query("SELECT * FROM location WHERE parentID = :id ORDER BY timeStamp ASC")
    fun getRoute(id: Long): Flow<List<Location>>


    @Query("DELETE FROM workout WHERE label = :label")
    suspend fun deleteWorkoutByLabel(label: String)

    @Query("DELETE FROM workoutLabel WHERE label = :label")
    suspend fun delete(label: String)

    /*
    averages
     */
    @Query("SELECT AVG(length) FROM workout WHERE label = :label")
    fun getAverageLengthByLabel(label: String): Flow<Double>

    @Query("SELECT AVG(length) FROM workout WHERE workoutType = :type")
    fun getAverageLengthByType(type: WorkoutType): Flow<Double>

    @Query("SELECT AVG(calories) FROM workout WHERE label = :label")
    fun getAverageCaloriesByLabel(label: String): Flow<Double>

    @Query("SELECT AVG(calories) FROM workout WHERE workoutType = :type")
    fun getAverageCaloriesByType(type: WorkoutType): Flow<Double>


    @Query("SELECT AVG(averages) FROM (SELECT AVG(value) as averages FROM hrlist WHERE parentID in" +
            " (SELECT timeStamp FROM workout WHERE workoutType = :type) GROUP BY parentID)")
    fun getAverageBPMByType(type: WorkoutType): Flow<Double>

    @Query("SELECT AVG(averages) FROM (SELECT AVG(value) as averages FROM hrlist WHERE parentID in" +
            " (SELECT timeStamp FROM workout WHERE label = :label) GROUP BY parentID)")
    fun getAverageBPMByLabel(label: String): Flow<Double>


    @Query("SELECT * FROM workoutLabel")
    fun getLabels(): Flow<List<WorkoutLabel>>


    @Query("SELECT * FROM workoutLabel WHERE workoutType = :workoutType")
    fun getLabelsByType(workoutType: WorkoutType):Flow<List<WorkoutLabel>>

    @Query("SELECT AVG(distance) FROM workout WHERE workoutType = :type")
    fun getAverageDistance(type: WorkoutType): Flow<Double>

    @Query("SELECT AVG(distance) FROM workout WHERE workoutType = :type and label = :label")
    fun getAverageDistanceByLabel(type: WorkoutType, label: String): Flow<Double>

    @Query("SELECT AVG(averageSpeed) FROM workout WHERE workoutType = :type")
    fun getAverageSpeed(type: WorkoutType): Flow<Double> //average average speed, :)

    @Query("SELECT AVG(averageSpeed) FROM workout WHERE workoutType = :type and label = :label")
    fun getAverageSpeedByLabel(type: WorkoutType, label: String): Flow<Double>

    @Query("SELECT COUNT(*) FROM workout WHERE label = :label")
    fun getNumberOfWorkoutsByLabel(label: String): Flow<Int>

}