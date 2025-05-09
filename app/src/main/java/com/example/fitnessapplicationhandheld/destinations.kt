package com.example.fitnessapplicationhandheld

import android.media.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface Destination {
    val route: String
    val topBarText: String
}

interface SelectableDestination: Destination{
    val selectedIcon: @Composable()(Dp)->Unit
    val icon: @Composable()(Dp)->Unit
}

object DestinationToday : SelectableDestination{
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {
            Icon(Icons.Filled.Home,null, modifier = Modifier.size(it))
        }
    override val icon: @Composable()(Dp)->Unit = {
        Icon(Icons.Outlined.Home,null, modifier = Modifier.size(it))
    }
    override val route = "todays_activity"
    override val topBarText = "Today's Activities"


}

object DestinationStats : SelectableDestination {
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {
            Icon(painterResource(R.drawable.graphiconfilled),null, modifier = Modifier.size(it))
        }
    override val icon: @Composable()(Dp)->Unit = {
        Icon(painterResource(R.drawable.graphicon),null, modifier = Modifier.size(it))
    }
    override val route = "workout_stats"
    override val topBarText = "Workout Statistics"
}

object DestinationHistory :SelectableDestination{
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {
            Icon(painterResource(R.drawable.filledhistoryicongood),null, modifier = Modifier.size(it))
        }
    override val icon: @Composable()(Dp)->Unit = {
        Icon(painterResource(R.drawable.historyicongood_removebg_preview),null, modifier = Modifier.size(it))
    }
    override val route = "workout_history"
    override val topBarText = "Workout History"
}

object DestinationWorkoutDetails : Destination {

    override val route = "workout_details"
    override val topBarText = "Workout Details"
}



object DestinationWorkoutLabels : SelectableDestination{
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {
            Icon(painterResource(R.drawable.selectedlisticon),null, modifier = Modifier.size(it))
        }
    override val icon: @Composable()(Dp)->Unit = {
        Icon(painterResource(R.drawable.listicon),null, modifier = Modifier.size(it))
    }

    override val route = "workout_labels"
    override val topBarText = "My Workouts"

}

object DestinationNewWorkoutLabel : Destination {

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