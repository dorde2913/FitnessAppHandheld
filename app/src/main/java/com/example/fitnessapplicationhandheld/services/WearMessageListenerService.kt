package com.example.fitnessapplicationhandheld.services

import android.health.connect.datatypes.units.Length
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.fitnessapplicationhandheld.CALS_KEY
import com.example.fitnessapplicationhandheld.STEPS_KEY
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.reflect.KClass


@AndroidEntryPoint
class WearMessageListenerService : WearableListenerService(){

    @Inject
    lateinit var repository: WorkoutRepository

    private val dataClient by lazy { Wearable.getDataClient(this)}


    private val timestampedHandlers: Map<String,(DataMap,Long)->Unit> =
        mapOf(
            Pair("heartrate") { dataMap , timeStamp ->

                val update =
                    deserializeUpdate<BPMUpdate>(dataMap.getByteArray("heartrate")) ?: return@Pair
                CoroutineScope(Dispatchers.IO).launch {
                    val workout = repository.getWorkout(update.id, update.label) ?: return@launch
                    repository.insertHRList(update.BPMList, workout.timestamp)
                    repository.updateLength(workout.timestamp,update.length)
                }
                Log.d(
                    "DataLayerListenerService",
                    "New heart rate list of length ${update.BPMList.size} received "
                )
            },

            Pair("location"){dataMap , timeStamp ->
                val locationUpdate = deserializeLocationUpdate(dataMap.getByteArray("location"))

                Log.d(
                    "DataLayerListenerService",
                    "New location list of length ${locationUpdate.locationList.size} received "
                )
            }

        )


    private val handlers: Map<String, (DataMap)->Unit> =
        mapOf(
            Pair("distance"){dataMap ->
                val distanceUpdate = deserializeDistanceUpdate(dataMap.getByteArray("distance"))
                CoroutineScope(Dispatchers.IO).launch {
                    val workout = repository.getWorkout(distanceUpdate.id, distanceUpdate.label) ?: return@launch
                    repository.updateDistance(workout.timestamp,distanceUpdate.distance)
                }
                Log.d("DataLayerListenerService", "Distance received ${distanceUpdate.distance}")
            },
            Pair("calories"){dataMap ->
                val caloriesUpdate = deserializeCaloriesUpdate(dataMap.getByteArray("calories"))
                CoroutineScope(Dispatchers.IO).launch {
                    val workout = repository.getWorkout(caloriesUpdate.id, caloriesUpdate.label) ?: return@launch
                    repository.updateCalories(workout.timestamp,caloriesUpdate.calories)
                }
                Log.d("DataLayerListenerService", "Calories received ${caloriesUpdate.calories}")
            },
            Pair("calories_daily"){dataMap ->
                val cals = dataMap.getInt("calories_daily")

                Log.d("DataLayerListenerService", "Calories received $cals")
                CoroutineScope(Dispatchers.IO).launch {
                    this@WearMessageListenerService.dataStore.edit { preferences->
                        preferences[CALS_KEY] = cals
                    }
                }
            },
            Pair("steps_daily"){dataMap ->
                val steps = dataMap.getInt("steps_daily")

                Log.d("DataLayerListenerService", "Daily Steps received $steps")
                CoroutineScope(Dispatchers.IO).launch {
                    this@WearMessageListenerService.dataStore.edit { preferences->
                        preferences[STEPS_KEY] = steps
                    }
                }
            },
        )



    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                println("DATA CHANGED")

                val dataItem = event.dataItem
                println(dataItem.uri.path)
                if (dataItem.uri.path == null) return

                val segments = dataItem.uri.path?.split("/") ?: return
                val dataMap = DataMapItem.fromDataItem(dataItem).dataMap

                if (segments.size > 2) {
                    val timestamp = segments[2]
                    val path = segments[1]

                    if (timestampedHandlers.containsKey(path)){
                        timestampedHandlers[path]!!(dataMap,timestamp.toLong())
                    }

                } else {
                    //size je 2, ili kalorije ili distanca
                    val path = segments[1]

                    if (handlers.containsKey(path)){
                        handlers[path]!!(dataMap)
                    }

                }
                deleteDataItem(dataItem.uri.path!!)
            }
        }
    }


    inline fun <reified T> deserializeUpdate(byteArray: ByteArray?): T?{
        if (byteArray == null) return null
        val jsonString = byteArray.toString(Charsets.UTF_8)
        return Json.decodeFromString(jsonString)
    }



    fun deserializeLocationUpdate(byteArray: ByteArray?): LocationUpdate{
        if (byteArray == null) return LocationUpdate()
        val jsonString = byteArray.toString(Charsets.UTF_8)
        return Json.decodeFromString(jsonString)
    }

    fun deserializeBPMUpdate(byteArray: ByteArray?): BPMUpdate{
        if (byteArray == null) return BPMUpdate()
        val jsonString = byteArray.toString(Charsets.UTF_8)
        return Json.decodeFromString(jsonString)
    }

    fun deserializeDistanceUpdate(byteArray: ByteArray?): DistanceUpdate{
        if (byteArray == null) return DistanceUpdate()
        val jsonString = byteArray.toString(Charsets.UTF_8)
        return Json.decodeFromString(jsonString)
    }

    fun deserializeCaloriesUpdate(byteArray: ByteArray?): CaloriesUpdate{
        if (byteArray == null) return CaloriesUpdate()
        val jsonString = byteArray.toString(Charsets.UTF_8)
        return Json.decodeFromString(jsonString)
    }

    fun deleteDataItem(path: String){
        val uri = Uri.Builder()
            .scheme("wear")
            .path(path)
            .build()

        dataClient.deleteDataItems(uri).addOnSuccessListener { dataItem->
            Log.d("DataClient", "DataItem deleted: $dataItem")
        }
            .addOnFailureListener { exception->
                Log.d("DataClient","Failed to delete: $exception")
            }
    }


}


@Serializable
data class BPMUpdate(
    val id: Long = 0,
    val label: String = "",
    val length: Long = 0,
    val BPMList: List<Int> = listOf()
)

@Serializable
data class LocationUpdate(
    val id: Long = 0,
    val label: String = "",
    val locationList: List<Pair<Double,Double>> = listOf()
)

@Serializable
data class CaloriesUpdate(
    val id: Long = 0,
    val label: String = "",
    val calories: Int = 0
)

@Serializable
data class DistanceUpdate(
    val id: Long = 0,
    val label: String = "",
    val distance: Int = 0
)
