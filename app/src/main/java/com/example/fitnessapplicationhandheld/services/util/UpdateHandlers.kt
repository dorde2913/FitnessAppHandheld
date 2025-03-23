package com.example.fitnessapplicationhandheld.services.util

import android.util.Log
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import com.google.android.gms.wearable.DataMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


/*
to track new data create object which extends UpdateHandler and add it to handlers map
 */
abstract class UpdateHandler {
    val loggerTag = "Wear Update"

    var timeStamp: Long = 0
    abstract val segmentNumber: Int
    abstract val key: String

    abstract fun deserializationWrapper(byteArray: ByteArray?): WearUpdate?

    fun handleUpdate(dataMap: DataMap, segments: List<String>, repository: WorkoutRepository): Int{
        if (segmentNumber > segments.size) return -1
        if (segments.size > 2) timeStamp = segments[2].toLong()

        val update = deserializationWrapper(dataMap.getByteArray(key)) ?: return -1

        storeUpdate(update, repository)

        return 0
    }
    protected abstract fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository)
    protected abstract fun logUpdate(value: Int)
}

inline fun <reified T> deserializeUpdate(byteArray: ByteArray?): T?{
    if (byteArray == null) return null
    val jsonString = byteArray.toString(Charsets.UTF_8)
    return Json.decodeFromString(jsonString)
}

object HeartRateHandler: UpdateHandler(){
    override val key: String
        get() = "heartrate"
    override val segmentNumber: Int
        get() = 3

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<BPMUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as BPMUpdate

        CoroutineScope(Dispatchers.IO).launch {
            val workout = repository.getWorkout(update.id, update.label) ?: return@launch
            repository.insertHRList(update.BPMList, workout.timestamp, timeStamp = timeStamp)
            repository.updateLength(workout.timestamp,update.length)
            repository.updateSpeed(workout.timestamp,workout.distance,workout.length)
        }

        logUpdate(update.BPMList.size)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag, "Received Heart Rate Update of size: $value")
    }


}


object LocationHandler: UpdateHandler(){
    override val key: String
        get() = "location"
    override val segmentNumber: Int
        get() = 3

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<LocationUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as LocationUpdate

        CoroutineScope(Dispatchers.IO).launch {
            val workout = repository.getWorkout(update.id, update.label) ?: return@launch
            repository.insertLocationList(update.locationList, workout.timestamp, timeStamp = timeStamp)
        }
        logUpdate(update.locationList.size)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Location Update of size: $value")
    }
}

object CaloriesHandler: UpdateHandler(){
    override val key: String
        get() = "calories"
    override val segmentNumber: Int
        get() = 2

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<CaloriesUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository
    ) {
        if (update == null) return
        update as CaloriesUpdate

        CoroutineScope(Dispatchers.IO).launch {
            val workout = repository.getWorkout(update.id, update.label) ?: return@launch
            repository.updateCalories(workout.timestamp,update.calories)
        }
        logUpdate(update.calories)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Calories Update: $value")
    }

}

object DistanceHandler: UpdateHandler(){
    override val key: String
        get() = "distance"
    override val segmentNumber: Int
        get() = 2

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<DistanceUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as DistanceUpdate
        CoroutineScope(Dispatchers.IO).launch {
            val workout = repository.getWorkout(update.id, update.label) ?: return@launch
            repository.updateDistance(workout.timestamp,update.distance)
            repository.updateSpeed(workout.timestamp,workout.distance,workout.length)
        }
        logUpdate(update.distance)

    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Distance Update: $value")
    }
}

object DailyStepsHandler: UpdateHandler(){
    override val key: String
        get() = "steps_daily"
    override val segmentNumber: Int
        get() = 2

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<DailyStepsUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as DailyStepsUpdate

        CoroutineScope(Dispatchers.IO).launch {
            repository.updateDailySteps(update.steps)
        }
        logUpdate(update.steps)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Daily Steps Update: $value")
    }

}

object DailyCaloriesHandler: UpdateHandler(){
    override val key: String
        get() = "calories_daily"
    override val segmentNumber: Int
        get() = 2

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<DailyCaloriesUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as DailyCaloriesUpdate

        CoroutineScope(Dispatchers.IO).launch {
            repository.updateDailyCalories(update.calories)
        }
        logUpdate(update.calories)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Daily Calories Update: $value")
    }
}

object DailyHeartRateHandler: UpdateHandler(){
    override val key: String
        get() = "daily_heartrate"
    override val segmentNumber: Int
        get() = 2

    override fun deserializationWrapper(byteArray: ByteArray?) =
        deserializeUpdate<DailyHeartRateUpdate>(byteArray)

    override fun storeUpdate(update: WearUpdate?, repository: WorkoutRepository) {
        if (update == null) return
        update as DailyHeartRateUpdate

        CoroutineScope(Dispatchers.IO).launch {
            update.hrList.forEachIndexed{index, value ->
                repository.updateDailyHR(index,value)
            }
        }
        logUpdate(update.hrList.size)
    }

    override fun logUpdate(value: Int) {
        Log.d(loggerTag,"Received Daily Heart Rate Update: $value")
    }
}


val handlers = mapOf(
    Pair(HeartRateHandler.key, HeartRateHandler),
    Pair(LocationHandler.key, LocationHandler),
    Pair(DistanceHandler.key, DistanceHandler),
    Pair(CaloriesHandler.key, CaloriesHandler),
    Pair(DailyStepsHandler.key, DailyStepsHandler),
    Pair(DailyCaloriesHandler.key, DailyCaloriesHandler),
    Pair(DailyHeartRateHandler.key, DailyHeartRateHandler)
)