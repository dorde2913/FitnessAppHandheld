package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutIcon
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutLabelIcon
import com.google.android.gms.maps.model.Circle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutLabelCard(
    modifier: Modifier = Modifier,
    workoutLabel: WorkoutLabel,
    cardColors: CardColors,
    viewModel: WorkoutViewModel,
    onHold: ()->Unit,
    selectMode: Boolean,
){

    val numberOfWorkouts by viewModel.getNumberOfWorkoutsByLabel(workoutLabel.label).collectAsState(initial = 0)
    var selected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectMode) {
        if (!selectMode) selected = false
    }

    Card(
        modifier = modifier
            .padding(10.dp)
            .heightIn(max = 180.dp)
            .combinedClickable(
                enabled = true,
                onLongClick = {
                    if (!selectMode) {
                        onHold()
                        selected = true
                        viewModel.deletionList.add(workoutLabel.label)
                    }
                },
                onClick = {
                    if (selectMode){
                        selected = !selected
                    }
                    if (selected) viewModel.deletionList.add(workoutLabel.label)
                    else viewModel.deletionList.remove(workoutLabel.label)
                }
            ),
        colors = cardColors,
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if (selectMode){
                Box(modifier = Modifier.fillMaxSize()){
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        if (selected)
                            Icon(Icons.Default.CheckCircle,null, modifier = Modifier.padding(5.dp).size(30.dp))
                        else
                            Box(modifier = Modifier.padding(5.dp).size(30.dp).clip(CircleShape).background(MaterialTheme.colorScheme.background))
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                WorkoutLabelIcon(
                    boxSize = 100,
                    workout = workoutLabel,
                    iconTint = cardColors.contentColor
                )
                Text(text = workoutLabel.label, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Text(text = if (numberOfWorkouts == 1) "1 workout" else "$numberOfWorkouts workouts",
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }

    }
}