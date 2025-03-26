package com.example.fitnessapplicationhandheld.stateholders

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
): ViewModel() {

    private val dataClient by lazy { Wearable.getDataClient(context)}
    val workouts = repository.workouts
    val labels = repository.labels


    var deletionList: MutableList<String> = mutableListOf()

    fun deleteSelectedLabels(){
        viewModelScope.launch {
            for (label in deletionList){
                //repository.deleteWorkoutByLabel(label)
                repository.deleteLabel(label)
            }
        }
        deletionList = mutableListOf()
    }

    val dailyHR = repository.dailyHR

    fun getHRList(parentId: Long) =
        repository.getHRList(parentId)

    fun getAverageBPM(parentId: Long) =
        repository.getAverageBPM(parentId)

    fun getCardioWorkouts() =
        repository.getCardioWorkouts()

    fun sendCalsGoal(cals: Int){
        val dataMapRequest = PutDataMapRequest.create("/cals_goal").apply{
            dataMap.putInt("cals_goal", cals)
        }
        val putDataRequest = dataMapRequest.asPutDataRequest().setUrgent()
        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem->
                Log.d("DataClient", "DataItem saved: $dataItem")
            }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to send: $exception")
            }
    }

    fun sendStepsGoal(steps: Int){
        val dataMapRequest = PutDataMapRequest.create("/steps_goal").apply{
            dataMap.putInt("steps_goal", steps)
        }
        val putDataRequest = dataMapRequest.asPutDataRequest().setUrgent()
        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem->
                Log.d("DataClient", "DataItem saved: $dataItem")
            }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to send: $exception")
            }
    }

    fun getRoute(id: Long) =
        repository.getRoute(id)

    fun getWorkout(id: Long) =
        repository.getWorkout(id)

    fun getAverageLengthByLabel(label: String) =
        repository.getAverageLengthByLabel(label)

    fun getAverageLengthByType(type: WorkoutType) =
        repository.getAverageLengthByType(type)

    fun getAverageCaloriesByLabel(label: String) =
        repository.getAverageCaloriesByLabel(label)

    fun getAverageCaloriesByType(type: WorkoutType) =
        repository.getAverageCaloriesByType(type)

    fun getAverageBPMByType(type: WorkoutType) =
        repository.getAverageBPMByType(type)

    fun getAverageBPMByLabel(label: String) =
        repository.getAverageBPMByLabel(label)

    fun getLabelsByType(type: WorkoutType) =
        repository.getLabelsByType(type)

    //cardio
    fun getAverageDistance()=
        repository.getAverageDistance()


    fun getAverageDistanceByLabel(label: String) =
        repository.getAverageDistanceByLabel(label = label)


    fun getAverageSpeed() =
        repository.getAverageSpeed()


    fun getAverageSpeedByLabel( label: String) =
        repository.getAverageSpeedByLabel(label = label)

    fun getNumberOfWorkoutsByLabel(label: String) =
        repository.getNumberOfWorkoutsByLabel(label)

    suspend fun insertWorkoutLabel(workoutLabel: WorkoutLabel) =
        repository.insertWorkoutLabel(workoutLabel)
}