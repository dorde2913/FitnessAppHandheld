package com.example.fitnessapplicationhandheld.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.WorkoutLabelCard

@Composable
fun WorkoutLabelsScreen(modifier: Modifier = Modifier,
                        viewModel: WorkoutViewModel,
                        cardColors: CardColors){
    val labels by viewModel.labels.collectAsState(initial = listOf())



    var selectMode by rememberSaveable { mutableStateOf(false) }

    if (selectMode){
        BackHandler {
            selectMode = false
            viewModel.deletionList = mutableListOf()
        }
    }




    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(
                        state = rememberScrollState(initial = 0)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                labels.chunked(2).forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WorkoutLabelCard(
                            modifier = Modifier.weight(0.5f),
                            workoutLabel = it[0],
                            cardColors = cardColors,
                            viewModel = viewModel,
                            onHold = { selectMode = true },
                            selectMode = selectMode
                        )
                        if (it.size > 1)
                            WorkoutLabelCard(
                                modifier = Modifier.weight(0.5f),
                                workoutLabel = it[1],
                                cardColors = cardColors,
                                viewModel = viewModel,
                                onHold = { selectMode = true },
                                selectMode = selectMode,
                            )
                        else {
                            Spacer(modifier.weight(0.5f))
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ){

            Box(modifier = Modifier.fillMaxSize()){
                if (selectMode)
                    Row(
                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(
                            onClick = {
                                selectMode = false
                                viewModel.deletionList = mutableListOf()
                            }
                        ) {
                            Icon(Icons.Default.Close, null, modifier = Modifier.size(30.dp))
                        }

                        IconButton(
                            onClick = {
                                viewModel.deleteSelectedLabels()
                                selectMode = false
                            }
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(30.dp))
                        }

                    }

            }

        }

    }



}
