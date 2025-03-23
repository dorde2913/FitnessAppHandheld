package com.example.fitnessapplicationhandheld.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.getComparison
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.BPMGraphCard
import com.example.fitnessapplicationhandheld.uicomponents.CardioStatsCard
import com.example.fitnessapplicationhandheld.uicomponents.Spinner
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutIcon
import com.example.fitnessapplicationhandheld.uicomponents.workout.formatDistance
import com.example.fitnessapplicationhandheld.uicomponents.workout.getIconSize
import com.example.fitnessapplicationhandheld.uicomponents.workout.getWorkoutIcon
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@Composable
fun WorkoutDetailsScreen(modifier: Modifier = Modifier, id: Long, cardColors: CardColors,
                         viewModel: WorkoutViewModel){

    val workout by viewModel.getWorkout(id).collectAsState(initial = Workout())

    if (workout != Workout()) {

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val length = LocalTime.ofNanoOfDay(workout.length * 1_000_000).format(formatter)


        val hrList by viewModel.getHRList(workout.timestamp).collectAsState(initial = listOf())

        val averageBPM =
            if (hrList.isEmpty()) 0.0 else hrList.map { it.value }.average()

        val averageBPMString = String.format("%.2f", averageBPM)


        val labels by viewModel.getLabelsByType(workout.workoutType)
            .collectAsState(initial = listOf())

        var all =
            if (workout.workoutType == WorkoutType.GYM) "All Gym Workouts"
            else "All Cardio Workouts"


        val options = labels.map { it.label }.plus(all)
            .zip(labels.map { it.color }.plus(0)).toMap()

        var comparison by rememberSaveable { mutableStateOf(all) }

        val comparisonLength by
        if (comparison == all)
            viewModel.getAverageLengthByType(WorkoutType.CARDIO).collectAsState(initial = 0.0)
        else viewModel.getAverageLengthByLabel(comparison).collectAsState(initial = 0.0)

        val comparisonCalories by
        if (comparison == all)
            viewModel.getAverageCaloriesByType(WorkoutType.CARDIO).collectAsState(initial = 0.0)
        else viewModel.getAverageCaloriesByLabel(comparison).collectAsState(initial = 0.0)

        val comparisonBPM by
        if (comparison == all)
            viewModel.getAverageBPMByType(WorkoutType.CARDIO).collectAsState(initial = 0.0)
        else viewModel.getAverageBPMByLabel(comparison).collectAsState(initial = 0.0)



        val lengthComparison = getComparison(workout.length.toDouble(), comparisonLength)
        val caloriesComparison = getComparison(workout.calories.toDouble(), comparisonCalories)
        val bpmComparison = getComparison(averageBPM, comparisonBPM)
        val locationList by viewModel.getRoute(workout.timestamp).collectAsState(initial = listOf())




        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(
                    state = rememberScrollState(initial = 0)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WorkoutIcon(
                    boxSize = 150,
                    workout = workout,
                    iconTint = cardColors.contentColor
                )

                Text(
                    text = workout.label,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = cardColors.contentColor,
                    fontSize = 24.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Basic stats", color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp
                )

                Spinner(
                    value = comparison,
                    onSelect = { comparison = it },
                    options = options.minus(comparison),
                    cardColors = cardColors
                )
            }

            //ovde dropdown za compare

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = cardColors,
            ) {
                BasicStatRow(
                    icon = R.drawable.stopwatchicon_removebg_preview,
                    iconSize = 50,
                    label = "Workout Length: ",
                    value = length,
                    diff = lengthComparison,
                    cardColors = cardColors
                )
                BasicStatRow(
                    icon = R.drawable.caloriesicon_removebg_preview,
                    iconSize = 40,
                    label = "Calories burned:",
                    value = "${workout.calories}kcal",
                    diff = caloriesComparison,
                    cardColors = cardColors
                )
                BasicStatRow(
                    icon = R.drawable.heartrateicon_removebg_preview,
                    iconSize = 25,
                    label = "Average Heart Rate:",
                    value = "${averageBPMString}bpm",
                    diff = bpmComparison,
                    cardColors = cardColors
                )
            }

            Text(
                "Heart Rate Over Workout Duration", color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                textAlign = TextAlign.Left,
                fontSize = 15.sp
            )
            BPMGraphCard(
                bpmList = hrList,
                cardColors = cardColors
            )
            if (workout.workoutType == WorkoutType.CARDIO){

                val comparisonDistance by
                if (comparison == all)
                    viewModel.getAverageDistance().collectAsState(initial = 0.0)
                else viewModel.getAverageDistanceByLabel(comparison).collectAsState(initial = 0.0)

                val comparisonSpeed by
                if (comparison == all)
                    viewModel.getAverageSpeed().collectAsState(initial = 0.0)
                else viewModel.getAverageSpeedByLabel(comparison).collectAsState(initial = 0.0)

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Cardio specific stats", color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp,
                )

                //Spacer(modifier = Modifier.height(40.dp))
                CardioStatsCard(
                    locationList = locationList,
                    cardColors = cardColors,
                    averageSpeed = "${ String.format("%.2f",workout.averageSpeed) }m/s",
                    distance = formatDistance(workout.distance),
                    distanceComparison = getComparison(workout.distance.toDouble(),comparisonDistance),
                    speedComparison = getComparison(workout.averageSpeed,comparisonSpeed)
                )
            }



        }
    }
}

@Composable
fun BasicStatRow(icon: Int, iconSize: Int, label: String, value: String,
                 cardColors: CardColors, diff: String){
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier.width(50.dp),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = cardColors.contentColor,
                    modifier = Modifier.size(iconSize.dp)
                )
            }

            Column(
                //verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = label,
                    fontSize = 12.sp)
                Text(text = value,
                    fontSize = 18.sp)
            }

        }
        Text(text = diff, color = if (diff[0] == '-' && diff[1] == '-') Color.Gray
                                else if (diff[0] == '-') Color.Red
                                else Color.Green,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 10.dp))
    }
}



