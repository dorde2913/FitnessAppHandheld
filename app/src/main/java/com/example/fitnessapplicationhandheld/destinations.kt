package com.example.fitnessapplicationhandheld

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val icon: ImageVector?
    val customIcon: Int?
    val route: String
    val topBarText: String
}

object DestinationToday : Destination {
    override val customIcon = null
    override val icon = Icons.Filled.Home
    override val route = "todays_activity"
    override val topBarText = "Today's Activities"
}

object DestinationStats : Destination {
    override val customIcon = R.drawable.graphicon
    override val icon = null
    override val route = "workout_stats"
    override val topBarText = "Workout Statistics"
}

object DestinationHistory : Destination {
    override val customIcon = R.drawable.historyicongood_removebg_preview
    override val icon = null
    override val route = "workout_history"
    override val topBarText = "Workout History"
}

object DestinationWorkoutDetails : Destination {
    override val customIcon = null
    override val icon = Icons.Default.Lock
    override val route = "workout_details"
    override val topBarText = "Workout Details"
}

object DestinationFitnessGoals : Destination {
    override val customIcon = R.drawable.goalsicon
    override val icon = null
    override val route = "fitness_goals"
    override val topBarText = "Fitness Goals"
}

object DestinationWorkoutLabels : Destination {
    override val customIcon = R.drawable.listicon
    override val icon = null
    override val route = "workout_labels"
    override val topBarText = "My Workouts"
}

val bottomNavigationDestinations = listOf(
    DestinationHistory,
    DestinationWorkoutLabels,
    DestinationToday,
    DestinationFitnessGoals,
    DestinationStats,
)


val Destinations = mapOf(Pair(DestinationHistory.route , DestinationHistory),
    Pair(DestinationToday.route , DestinationToday),
    Pair(DestinationStats.route , DestinationStats),
    Pair(DestinationWorkoutDetails.route,DestinationWorkoutDetails),
    Pair(DestinationWorkoutLabels.route , DestinationWorkoutLabels),
    Pair(DestinationFitnessGoals.route , DestinationFitnessGoals))