package com.example.fitnessapplicationhandheld.uicomponents.workout

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.formatDistance
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@Composable
fun WorkoutRow(modifier: Modifier = Modifier, workout: Workout, cardColors: CardColors,
               averageBPM: Int, onWorkoutClick: (Long)->Unit){

    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(workout.timestamp),
        ZoneId.systemDefault())


    val dateFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
    val formatterHourMinute = DateTimeFormatter.ofPattern("HH:mm")
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")


    val length = LocalTime.ofNanoOfDay(workout.length * 1_000_000).format(formatter)

    val speed = if (workout.length != 0L) workout.distance / ((workout.length.toDouble()  / 1000))
                else 0f


    Row(
        modifier = modifier.fillMaxWidth()
            .clickable{
                onWorkoutClick(workout.timestamp)
            }
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = workout.label, fontSize = 15.sp, textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 70.dp).basicMarquee(
                    iterations = 77,
                    repeatDelayMillis = 5000
                ))
            WorkoutIcon(
                boxSize = 70,
                workout = workout,
                iconTint = cardColors.contentColor
            )
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


                Row(horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.caloriesicon_removebg_preview),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(35.dp))
                    Text("${workout.calories}kcal")
                }
                Row(horizontalArrangement = Arrangement.Start,
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

                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.distanceicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(25.dp))
                        Text(formatDistance(workout.distance))
                    }
                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.speedicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(35.dp))
                        Text("${String.format("%.2f",speed)}m/s")
                    }
                }
            }
        }
    }
}


//same thing as above, but not clickable
@SuppressLint("DefaultLocale")
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

    val speed = if (workout.length != 0L) workout.distance / ((workout.length.toDouble()  / 1000))
    else 0f


    Row(
        modifier = modifier.fillMaxWidth()
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = workout.label, fontSize = 15.sp, textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 70.dp).basicMarquee(
                    iterations = 77,
                    repeatDelayMillis = 5000
                ))
            WorkoutIcon(
                boxSize = 70,
                workout = workout,
                iconTint = cardColors.contentColor
            )
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


                Row(horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.caloriesicon_removebg_preview),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(35.dp))
                    Text("${workout.calories}kcal")
                }
                Row(horizontalArrangement = Arrangement.Start,
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

                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.distanceicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(25.dp))
                        Text(formatDistance(workout.distance))
                    }
                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically){
                        Icon(painter = painterResource(R.drawable.speedicon_removebg_preview),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(35.dp))
                        Text("${String.format("%.2f",speed)}m/s")
                    }
                }
            }
        }
    }
}

