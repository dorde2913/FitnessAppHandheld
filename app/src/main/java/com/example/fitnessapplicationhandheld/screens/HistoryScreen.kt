package com.example.fitnessapplicationhandheld.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutCard
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HistoryScreen(modifier: Modifier = Modifier, viewModel: WorkoutViewModel, cardColors: CardColors,
                  onWorkoutClick: (Long)->Unit, snackbarState: SnackbarHostState){

    val workouts by viewModel.workouts.collectAsState(initial = listOf())


    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(BottomAppBarDefaults.containerColor)
    ) {
        item{
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(workouts, key = {it.timestamp}){workout ->
            val averageBPM by viewModel.getAverageBPM(workout.timestamp).collectAsState(initial = 0)
            var removed by rememberSaveable{mutableStateOf(false)}


            val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    println("value change")
                    if (it == SwipeToDismissBoxValue.EndToStart
                        && !removed){

                        removed = true
                        CoroutineScope(Dispatchers.IO).launch {

                            println("showing snackbar")
                            val result = snackbarState.showSnackbar(
                                message = "Deleted workout! :D",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            //delay(1500)
                            if (result != SnackbarResult.ActionPerformed){
                                viewModel.deleteWorkout(workout.timestamp)
                            }
                            else {
                                removed = false
                            }


                            //result contains whether or not the undo button was pressed
                        }
                    }
                    println("value: $it")
                    true
                    //it == SwipeToDismissBoxValue.EndToStart
                    //true
                },
                positionalThreshold = {
                    println(it)
                    it/2
                }
            )



            AnimatedVisibility(
                visible = !removed,
                exit = shrinkVertically (
                    animationSpec = tween(1000),
                    shrinkTowards = Alignment.Top
                ) + fadeOut()
            ) {
                SwipeToDismissBox(
                    backgroundContent = {DeleteBackground()},
                    state = swipeToDismissBoxState,
                    enableDismissFromStartToEnd = false,
                ) {

                    LaunchedEffect(removed) {
                        if (!removed) swipeToDismissBoxState.reset()
                    }


                    WorkoutCard(
                        workout = workout,
                        averageBPM = averageBPM,
                        cardColors = cardColors,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        onWorkoutClick = onWorkoutClick
                    )
                }
            }

        }
    }
}


@Composable
fun DeleteBackground(

) {

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(CardDefaults.shape)
            .fillMaxSize()
            .background(Color.Red)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

