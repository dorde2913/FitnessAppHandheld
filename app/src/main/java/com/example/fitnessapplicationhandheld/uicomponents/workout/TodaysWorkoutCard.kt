package com.example.fitnessapplicationhandheld.uicomponents.workout

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TodaysWorkoutCard(modifier: Modifier = Modifier, cardColors: CardColors,
                      viewModel: WorkoutViewModel, onWorkoutClick: (Long)->Unit){


    val workouts by viewModel.workouts.collectAsState(initial = listOf())
    val dateFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
    val todaysWorkouts =  workouts.filter {workout ->
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(workout.timestamp),
            ZoneId.systemDefault())
        val todaysDate = LocalDateTime.ofInstant(
            Instant.now(),
            ZoneId.systemDefault())

        dateTime.format(dateFormatter) == todaysDate.format(dateFormatter)

    }



    Card(
        modifier = modifier.fillMaxWidth(),
        colors = cardColors
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Today's workouts", modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Left)

            Column(
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 100.dp)
                    .heightIn(max = 120.dp)
                    .verticalScroll(state = rememberScrollState(initial = 0))
                    .animateContentSize(animationSpec = tween(durationMillis = 500))
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                if (todaysWorkouts.isEmpty())Text("No workouts recorded today")
                else{
                    HorizontalDivider(color = cardColors.contentColor)
                    todaysWorkouts.forEachIndexed { index, workout ->

                        val averageBPM by viewModel.getAverageBPM(workout.timestamp).collectAsState(initial = 0)


                        WorkoutRow(workout = workout, modifier = Modifier.padding(5.dp), cardColors = cardColors,
                            averageBPM = averageBPM, onWorkoutClick = onWorkoutClick)

                        if (index != todaysWorkouts.size -1)
                            HorizontalDivider(color = cardColors.contentColor)

                    }

                }
            }

        }
    }
}