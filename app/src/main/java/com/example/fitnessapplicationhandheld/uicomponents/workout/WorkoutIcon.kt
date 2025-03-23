package com.example.fitnessapplicationhandheld.uicomponents.workout

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import kotlin.math.roundToInt

@Composable
fun WorkoutIcon(boxSize: Int, workout: Workout, iconTint: Color){
    val ratio =
        if (workout.workoutType == WorkoutType.GYM) 1f
        else 0.7f
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(boxSize.dp)
            .background(Color.Transparent)
            .border(
                border = BorderStroke(3.dp, Color(workout.color)),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(getWorkoutIcon(workout.workoutType)),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size((boxSize * ratio).roundToInt().dp)
        )
    }
}