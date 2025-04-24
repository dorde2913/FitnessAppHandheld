package com.example.fitnessapplicationhandheld.repositories

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.edit
import androidx.room.Query
import com.example.fitnessapplicationhandheld.CALS_KEY
import com.example.fitnessapplicationhandheld.STEPS_KEY
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.database.Dao
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Location
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
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
    val labels = dao.getLabels()
    val dailyHR = dao.getDailyHR()

    val workoutsOrderedByLabel = dao.getWorkoutsOrderedByLabel()
    val labelsOrdered = dao.getLabelsOrdered()

    fun getAverageBPM(parentId: Long) =
        dao.getAverageBPM(parentId)

    suspend fun deleteHRList() =
        dao.deleteHRList()
    fun getOrderedLabelsByType(type: WorkoutType) =
        dao.getOrderedLabelsByType(type)

    suspend fun deleteWorkoutByLabel(label: String) =
        dao.deleteWorkoutByLabel(label)

    suspend fun deleteLabel(label: String) =
        dao.delete(label)



    fun getRoute(id: Long) =
        dao.getRoute(id)

    fun getAverageBPM() =
        dao.getAverageBPM()

    fun getWorkoutsByLabel(label: String) =
        dao.getWorkoutsByLabel(label)

    suspend fun insert(workout: Workout) =
        dao.insert(workout)


    fun insertLocationList(locationList: List<Pair<Double,Double>>, parentId: Long, timeStamp: Long) =
        CoroutineScope(Dispatchers.IO).launch {
            for (value in locationList){
                dao.insert(Location(parentID = parentId, latitude = value.first, longitude = value.second,
                    timeStamp = timeStamp))
            }
        }

    suspend fun deleteWorkout(id: Long) =
        dao.deleteWorkoutByID(id)
    fun getCardioWorkouts() =
        dao.getWorkoutsByType(WorkoutType.CARDIO)

    fun getWorkoutsByType(type: WorkoutType) =
        dao.getWorkoutsByType(type)



    fun getHRList(parentId: Long) =
        dao.getHRList(parentId)

    fun insertHRList(hrList: List<Int>, parentId: Long, timeStamp: Long) =
        CoroutineScope(Dispatchers.IO).launch {
            for (value in hrList){
                dao.insert(HRList(parentID = parentId, value = value, timeStamp = timeStamp))
            }
        }

    suspend fun updateLength(workoutID: Long, length: Long) =
        dao.updateLength(id = workoutID, value = length)

    suspend fun updateCalories(workoutID: Long, calories: Int) =
        dao.updateCalories(id = workoutID, value = calories)

    suspend fun updateDistance(workoutID: Long, distance: Int) =
        dao.updateDistance(id = workoutID, value = distance)

    suspend fun updateSpeed(workoutID: Long,distance: Int,length: Long) {
        var speed = 0.0
        println(length)
        if (distance != 0 && length != 0L)
            speed = distance / (length * 1_000_000_000).toDouble()

        dao.updateSpeed(workoutID,speed)
    }

    suspend fun updateDailySteps(steps: Int){
        context.dataStore.edit { preferences->
            preferences[STEPS_KEY] = steps
        }
    }

    suspend fun updateDailyCalories(calories: Int){
        context.dataStore.edit { preferences->
            preferences[CALS_KEY] = calories
        }
    }

    suspend fun updateDailyHR(hour: Int, value: Int) =
        dao.updateDailyHR(hour = hour, value = value)

    fun getWorkout(id: Long) =
        dao.getWorkoutFlow(id)

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


    fun getAverageLengthByLabel(label: String) =
        dao.getAverageLengthByLabel(label)

    fun getAverageLengthByType(type: WorkoutType) =
        dao.getAverageLengthByType(type)

    fun getAverageCaloriesByLabel(label: String) =
        dao.getAverageCaloriesByLabel(label)

    fun getAverageCaloriesByType(type: WorkoutType) =
        dao.getAverageCaloriesByType(type)

    fun getAverageBPMByType(type: WorkoutType) =
        dao.getAverageBPMByType(type)

    fun getAverageBPMByLabel(label: String) =
        dao.getAverageBPMByLabel(label)

    fun getLabelsByType(workoutType: WorkoutType) =
        dao.getLabelsByType(workoutType)



    //cardio specific
    fun getAverageDistance()=
        dao.getAverageDistance(type = WorkoutType.CARDIO)


    fun getAverageDistanceByLabel(label: String) =
        dao.getAverageDistanceByLabel(type = WorkoutType.CARDIO,label = label)


    fun getAverageSpeed() =
        dao.getAverageSpeed(type = WorkoutType.CARDIO)


    fun getAverageSpeedByLabel( label: String) =
        dao.getAverageSpeedByLabel(type = WorkoutType.CARDIO,label = label)

    fun getNumberOfWorkoutsByLabel(label: String) =
        dao.getNumberOfWorkoutsByLabel(label)

    suspend fun insertWorkoutLabel(workoutLabel: WorkoutLabel) =
        dao.insert(workoutLabel)
}