package com.example.fitnessapplicationhandheld.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel

@Composable
fun HistoryScren(modifier: Modifier = Modifier, viewModel: WorkoutViewModel){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        Text(text = "OVO JE EKRAN ZA ISTORIJU VEZBI")
    }
}