package com.example.fitnessapplicationhandheld

import android.annotation.SuppressLint
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
fun getComparison(first: Double, second: Double): String {
    println("comparison: first = $first, second = $second")
    if (first == 0.0 || second == 0.0 ||
        first == second) return "--"

    val sign = if (first > second)"+" else ""

    return "$sign${ String.format("%.2f",(first / second) * 100 - 100) }%"
}

//maybe make date/time formatting methods here to reduce duplicate code


fun formatLength(length: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return LocalTime.ofNanoOfDay(length * 1_000_000).format(formatter)
}


fun formatDistance(distance: Int): String {
    val kilometers = distance/1000
    val meters = distance%1000

    return when {
        kilometers > 0 -> "$kilometers.${meters}km"
        else -> "${meters}m"
    }
}

fun getWorkoutIcon(workoutType: WorkoutType): Int {
    println(workoutType)
    return when (workoutType){
        WorkoutType.GYM -> R.drawable.jimjpeg_removebg_preview
        WorkoutType.CARDIO -> R.drawable.running1_removebg_preview
    }
}