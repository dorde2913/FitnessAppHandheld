package com.example.fitnessapplicationhandheld.screens

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.ui.theme.FitnessApplicationHandheldTheme
import com.example.fitnessapplicationhandheld.uicomponents.StepsCard
import com.example.fitnessapplicationhandheld.uicomponents.TodaysHeartRateCard
import com.example.fitnessapplicationhandheld.uicomponents.TodaysWorkoutCard
import java.time.Instant

@Composable
fun TodaysActivityScreen(modifier: Modifier = Modifier, cardColors: CardColors,
                         viewModel: WorkoutViewModel){


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

        TodaysWorkoutCard(modifier = modifier.padding(10.dp), cardColors = cardColors, viewModel = viewModel)

        TodaysHeartRateCard(modifier = modifier.padding(10.dp), cardColors = cardColors)

    }
}
