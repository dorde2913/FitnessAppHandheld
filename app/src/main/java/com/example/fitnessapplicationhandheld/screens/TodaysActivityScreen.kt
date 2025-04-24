package com.example.fitnessapplicationhandheld.screens

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitnessapplicationhandheld.MainActivity
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.StepsCard
import com.example.fitnessapplicationhandheld.uicomponents.TodaysHeartRateCard
import com.example.fitnessapplicationhandheld.uicomponents.workout.TodaysWorkoutCard


@Composable
fun TodaysActivityScreen(modifier: Modifier = Modifier, cardColors: CardColors,
                         viewModel: WorkoutViewModel, onWorkoutClick: (Long)->Unit){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ){
        Spacer(modifier = Modifier.height(10.dp))

        StepsCard(
            modifier = Modifier.padding(10.dp),
            cardColors = cardColors,
            viewModel = viewModel
        )

        TodaysWorkoutCard(modifier = modifier.padding(10.dp), cardColors = cardColors, viewModel = viewModel, onWorkoutClick = onWorkoutClick)

        TodaysHeartRateCard(modifier = modifier.padding(10.dp), cardColors = cardColors, viewModel = viewModel)
    }
}
