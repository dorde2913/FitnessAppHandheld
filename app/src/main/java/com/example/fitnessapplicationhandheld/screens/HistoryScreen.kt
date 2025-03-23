package com.example.fitnessapplicationhandheld.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutCard
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutRow


@Composable
fun HistoryScreen(modifier: Modifier = Modifier, viewModel: WorkoutViewModel, cardColors: CardColors,
                  onWorkoutClick: (Long)->Unit){

    val workouts by viewModel.workouts.collectAsState(initial = listOf())


    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(BottomAppBarDefaults.containerColor)
    ) {
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(workouts){workout ->
            val averageBPM by viewModel.getAverageBPM(workout.timestamp).collectAsState(initial = 0)
            WorkoutCard(workout = workout, averageBPM = averageBPM,
                cardColors = cardColors, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                onWorkoutClick = onWorkoutClick)
        }
    }
}




