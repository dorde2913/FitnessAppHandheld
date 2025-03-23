package com.example.fitnessapplicationhandheld.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.StepsCard
import com.example.fitnessapplicationhandheld.uicomponents.TodaysHeartRateCard
import com.example.fitnessapplicationhandheld.uicomponents.workout.TodaysWorkoutCard

@Composable
fun TodaysActivityScreen(modifier: Modifier = Modifier, cardColors: CardColors,
                         viewModel: WorkoutViewModel, onWorkoutClick: (Long)->Unit){


    /*
    Ovde su neke temp vrednosti za testiranje UI, posle ce to sve da se uzima iz baze vrv
     */

    val todaysSteps = 587
    val stepsGoal = 3000

    val todaysCals = 700
    val calsGoal = 2000


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ){
        Spacer(modifier = Modifier.height(10.dp))

        StepsCard(
            modifier = Modifier.padding(10.dp),
            stepsGoal = stepsGoal,
            goalCals = calsGoal,
            cardColors = cardColors,
            viewModel = viewModel
        )

        TodaysWorkoutCard(modifier = modifier.padding(10.dp), cardColors = cardColors, viewModel = viewModel, onWorkoutClick = onWorkoutClick)

        TodaysHeartRateCard(modifier = modifier.padding(10.dp), cardColors = cardColors, viewModel = viewModel)

    }
}
