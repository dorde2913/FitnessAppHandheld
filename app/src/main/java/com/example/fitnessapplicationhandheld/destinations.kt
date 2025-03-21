package com.example.fitnessapplicationhandheld

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val icon: ImageVector
    val route: String
    val topBarText: String
}

object DestinationToday : Destination {
    override val icon = Icons.Default.Lock
    override val route = "todays_activity"
    override val topBarText = "Today's Activities"
}

object DestinationStats : Destination {
    override val icon = Icons.Default.Lock
    override val route = "workout_stats"
    override val topBarText = "Workout Statistics"
}

object DestinationHistory : Destination {
    override val icon = Icons.Default.Lock
    override val route = "workout_history"
    override val topBarText = "Workout History"
}



val Destinations = mapOf(Pair(DestinationHistory.route , DestinationHistory),
    Pair(DestinationToday.route , DestinationToday),
    Pair(DestinationStats.route , DestinationStats))