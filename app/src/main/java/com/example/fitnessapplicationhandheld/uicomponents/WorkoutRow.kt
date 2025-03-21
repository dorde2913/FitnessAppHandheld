package com.example.fitnessapplicationhandheld.uicomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutRow(modifier: Modifier = Modifier, workout: Workout, cardColors: CardColors,
               averageBPM: Int){

    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(workout.timestamp),
        ZoneId.systemDefault())


    val dateFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
    val formatterHourMinute = DateTimeFormatter.ofPattern("HH:mm")
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")


    val length = LocalTime.ofNanoOfDay(workout.length * 1_000_000).format(formatter)


    Row(
        modifier = modifier.fillMaxWidth()
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = workout.label, fontSize = 15.sp, textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 70.dp).basicMarquee(
                    repeatDelayMillis = 5000
                ))
            Box(
                modifier = Modifier.clip(CircleShape).size(70.dp).background(Color.Transparent)
                    .border(border = BorderStroke(3.dp,Color(workout.color)), shape = CircleShape),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(getWorkoutIcon(workout.workoutType)),
                    contentDescription = null,
                    tint = cardColors.contentColor,
                    modifier = Modifier.size(getIconSize(workout.workoutType).dp)
                )
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth()
        ){

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(R.drawable.stopwatchicon_removebg_preview),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(35.dp))
                Text(length)
                //Text(hrList.size.toString())

                Text(text = "${dateTime.format(dateFormatter)} at ${dateTime.format(formatterHourMinute)}",
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right)
            }





            Row(//red sa
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){


                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.caloriesicon_removebg_preview),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(35.dp))
                    Text("${workout.calories}kcal")
                }
                Row(horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.heartrateicon_removebg_preview),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp))
                    //funkcija sto racuna avg
                    Text("${averageBPM}bpm")
                }

            }
            if (workout.workoutType == WorkoutType.CARDIO){
                Row(//red sa
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){

                    Row(horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.distanceicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(25.dp))
                        Text(formatDistance(workout.distance))
                    }
                    Row(horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.speedicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(35.dp))
                        Text("${workout.averageSpeed}m/s")
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, secs)
        minutes > 0 -> String.format("%d:%02d", minutes, secs)
        else -> String.format("0:%02d", secs) // Ensures at least "0:SS" is displayed
    }
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


fun getIconSize(workoutType: WorkoutType): Int =
    when (workoutType){
        WorkoutType.GYM -> 70
        WorkoutType.CARDIO -> 50
    }

