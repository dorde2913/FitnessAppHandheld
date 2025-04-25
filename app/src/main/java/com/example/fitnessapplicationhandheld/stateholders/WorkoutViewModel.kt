package com.example.fitnessapplicationhandheld.stateholders

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
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


    var deletionList: MutableList<String> = mutableListOf()//labels

    var workoutDeletionList: MutableList<Long> = mutableListOf()



    fun getMostRecentWorkout() =
        repository.getMostRecentWorkout()

    fun deleteSelectedLabels(){
        viewModelScope.launch {
            for (label in deletionList){
                repository.deleteWorkoutByLabel(label)
                repository.deleteLabel(label)
            }
        }
        deletionList = mutableListOf()
        sendWorkoutLabels()
        updateWatchStats()
    }

    suspend fun deleteWorkout(id: Long) {
        repository.deleteWorkout(id)
        repository.deleteHRList()
        updateWatchStats()
    }


    fun getOrderedLabelsByType(type: WorkoutType) =
        repository.getOrderedLabelsByType(type)

    fun getWorkoutsByLabel(label: String) =
        repository.getWorkoutsByLabel(label)

    val dailyHR = repository.dailyHR

    fun getHRList(parentId: Long) =
        repository.getHRList(parentId)

    fun getAverageBPM(parentId: Long) =
        repository.getAverageBPM(parentId)

    fun getCardioWorkouts() =
        repository.getCardioWorkouts()
    fun getWorkoutsByType(type: WorkoutType) =
        repository.getWorkoutsByType(type)

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


    private fun sendToWatch(data: ByteArray, label: String){

        val dataMapRequest = PutDataMapRequest.create("/$label").apply{
            dataMap.putByteArray(label, data)
        }
        val putDataRequest = dataMapRequest.asPutDataRequest().setUrgent()

        val uri = Uri.Builder()
            .scheme("wear")
            .path(label)
            .build()

        dataClient.deleteDataItems(uri).addOnSuccessListener { dataItem->
            Log.d("DataClient", "DataItem deleted: $dataItem")
        }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to delete: $exception")
            }


        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener { dataItem->
                Log.d("DataClient", "DataItem saved: $dataItem")
            }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to send: $exception")
            }
    }

    private fun updateWatchStats(){

        CoroutineScope(Dispatchers.IO).launch {
            combine(repository.workouts,
                repository.getCardioWorkouts(),
                repository.getAverageBPM(),
            ){ allWorkouts, cardioWorkouts, averageBPM ->
                SimpleStats(
                    length = allWorkouts.map{it.length}.average().toLong(),
                    calories = allWorkouts.map { it.calories }.average().toLong(),
                    bpm = averageBPM.toLong(),
                    distance = cardioWorkouts.map { it.distance }.average().toLong(),
                    numWorkouts = allWorkouts.size.toLong(),
                    numCardio = cardioWorkouts.size.toLong()
                )
            }.collectLatest {
                sendToWatch(Json.encodeToString(it).toByteArray(), "simple_stats")
            }
        }
    }

    private fun sendWorkoutLabels(){

        viewModelScope.launch {
            val buh = labels.collectLatest {
                Log.d("test",it[0].label)
                val dataMapRequest = PutDataMapRequest.create("/workout_labels").apply{
                    dataMap.putByteArray("workout_labels", Json.encodeToString(LabelList(list = it)).toByteArray())
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

    suspend fun insertWorkoutLabel(workoutLabel: WorkoutLabel) {
        repository.insertWorkoutLabel(workoutLabel)
        sendWorkoutLabels()
    }

}

@Serializable
data class LabelList(
    val list : List<WorkoutLabel> = listOf()
)

@Serializable
data class SimpleStats(
    val length: Long,
    val calories: Long,
    val numWorkouts: Long,
    val numCardio: Long,
    val distance: Long,
    val bpm: Long
)