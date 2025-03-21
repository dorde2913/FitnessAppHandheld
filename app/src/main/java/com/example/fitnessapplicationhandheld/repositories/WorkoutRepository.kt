package com.example.fitnessapplicationhandheld.repositories

import android.content.Context
import androidx.compose.runtime.collectAsState
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.database.Dao
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Workout
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkoutRepository @Inject constructor(
    private val dao: Dao,
    @ApplicationContext private val context: Context
) {

    val workouts = dao.getWorkouts()


    fun getAverageBPM(parentId: Long) =
        dao.getAverageBPM(parentId)

    fun getWorkoutsByLabel(label: String) =
        dao.getWorkoutsByLabel(label)

    suspend fun insert(workout: Workout) =
        dao.insert(workout)


    fun getHRList(parentId: Long) =
        dao.getHRList(parentId)

    fun insertHRList(hrList: List<Int>, parentId: Long) =
        CoroutineScope(Dispatchers.IO).launch {
            for (value in hrList){
                dao.insert(HRList(parentID = parentId, value = value))
            }
        }

    suspend fun updateLength(workoutID: Long, length: Long) =
        dao.updateLength(id = workoutID, value = length)

    suspend fun updateCalories(workoutID: Long, calories: Int) =
        dao.updateCalories(id = workoutID, value = calories)

    suspend fun updateDistance(workoutID: Long, distance: Int) =
        dao.updateDistance(id = workoutID, value = distance)


    suspend fun getWorkout(id: Long, label: String): Workout?{
        //get/create workout
        if (id == 0L) return null
        val workout: Workout? =
            CoroutineScope(Dispatchers.IO).async {
                println("label: $label")
                val temp = dao.getWorkout(id)
                if (temp == null) {
                    val workoutLabel = dao.getWorkoutLabel(label)
                    if (workoutLabel == null){
                        println("workout label je null")
                        return@async null //vraca null
                    }
                    dao.insert(Workout(
                        timestamp = id, label = workoutLabel.label, color = workoutLabel.color, workoutType = workoutLabel.workoutType
                    ))
                }

                dao.getWorkout(id)
            }.await()
        if (workout == null) println("workout je null izvan korutine")
        return workout

    }



}