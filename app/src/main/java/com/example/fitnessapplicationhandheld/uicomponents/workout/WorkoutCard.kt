package com.example.fitnessapplicationhandheld.uicomponents.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout

@Composable
fun WorkoutCard(workout: Workout, averageBPM: Int, modifier: Modifier = Modifier,
                cardColors: CardColors, onWorkoutClick: (Long)->Unit){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .clickable{
                onWorkoutClick(workout.timestamp)
            },
        colors = cardColors
    ) {
        WorkoutRow(
            workout = workout,
            averageBPM = averageBPM,
            cardColors = cardColors,
            onWorkoutClick = onWorkoutClick,
            modifier = Modifier.padding(10.dp)
        )
    }
}