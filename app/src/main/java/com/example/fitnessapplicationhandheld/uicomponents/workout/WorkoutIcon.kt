package com.example.fitnessapplicationhandheld.uicomponents.workout

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
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
@Composable
fun WorkoutLabelIcon(modifier: Modifier = Modifier,
    boxSize: Int, workout: WorkoutLabel, iconTint: Color){
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

@Composable
fun WorkoutLabelIcon(modifier: Modifier = Modifier,
                     boxSize: Int, workoutType: WorkoutType,
                     iconTint: Color, color: Int, onClick: ()->Unit){
    val ratio =
        if (workoutType == WorkoutType.GYM) 1f
        else 0.7f
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = modifier
                .clip(CircleShape)
                .size(boxSize.dp)
                .background(Color.Transparent)
                .border(
                    border = BorderStroke(3.dp, Color(color)),
                    shape = CircleShape
                ).clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(getWorkoutIcon(workoutType)),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size((boxSize * ratio).roundToInt().dp)
            )
        }

    }

}