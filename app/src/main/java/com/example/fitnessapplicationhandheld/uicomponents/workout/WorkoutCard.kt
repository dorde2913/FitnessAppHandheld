package com.example.fitnessapplicationhandheld.uicomponents.workout

import android.graphics.Paint.Align
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutCard(workout: Workout, averageBPM: Int, modifier: Modifier = Modifier,
                cardColors: CardColors, onWorkoutClick: (Long)->Unit){



    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .combinedClickable(
                onClick = {
                    onWorkoutClick(workout.timestamp)
                },
            )
            ,
        colors = cardColors
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            WorkoutRow(
                workout = workout,
                averageBPM = averageBPM,
                cardColors = cardColors,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

