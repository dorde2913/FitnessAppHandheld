package com.example.fitnessapplicationhandheld

import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val icon: ImageVector?
    val customIcon: Int?
    val route: String
    val topBarText: String
}

interface SelectableDestination: Destination{
    val selectedIcon: ImageVector?
    val selectedCustomIcon: Int?
}

object DestinationToday : SelectableDestination{
    override val customIcon = null
    override val icon = Icons.Outlined.Home
    override val route = "todays_activity"
    override val topBarText = "Today's Activities"
    override val selectedIcon: ImageVector
        get() = Icons.Filled.Home
    override val selectedCustomIcon: Int?
        get() = null
}

object DestinationStats : SelectableDestination {
    override val customIcon = R.drawable.graphicon
    override val icon = null
    override val route = "workout_stats"
    override val topBarText = "Workout Statistics"
    override val selectedIcon: ImageVector?
        get() = null
    override val selectedCustomIcon: Int
        get() = R.drawable.graphiconfilled
}

object DestinationHistory :SelectableDestination{
    override val customIcon = R.drawable.historyicongood_removebg_preview
    override val icon = null
    override val route = "workout_history"
    override val topBarText = "Workout History"
    override val selectedIcon: ImageVector?
        get() = null
    override val selectedCustomIcon: Int
        get() = R.drawable.filledhistoryicongood
}

object DestinationWorkoutDetails : Destination {
    override val customIcon = null
    override val icon = Icons.Default.Lock
    override val route = "workout_details"
    override val topBarText = "Workout Details"
}



object DestinationWorkoutLabels : SelectableDestination{
    override val customIcon = R.drawable.listicon
    override val icon = null
    override val route = "workout_labels"
    override val topBarText = "My Workouts"
    override val selectedIcon: ImageVector?
        get() = null
    override val selectedCustomIcon: Int
        get() = R.drawable.selectedlisticon
}

object DestinationNewWorkoutLabel : Destination {
    override val customIcon = R.drawable.listicon
    override val icon = null
    override val route = "new_workout"
    override val topBarText = "New Workout"
}

val bottomNavigationDestinations = listOf(
    DestinationToday,
    DestinationHistory,
    DestinationWorkoutLabels,
    DestinationStats,
)


val Destinations = mapOf(Pair(DestinationHistory.route , DestinationHistory),
    Pair(DestinationToday.route , DestinationToday),
    Pair(DestinationStats.route , DestinationStats),
    Pair(DestinationWorkoutDetails.route,DestinationWorkoutDetails),
    Pair(DestinationWorkoutLabels.route , DestinationWorkoutLabels),
    Pair(DestinationNewWorkoutLabel.route , DestinationNewWorkoutLabel))