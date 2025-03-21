package com.example.fitnessapplicationhandheld.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapplicationhandheld.DestinationHistory
import com.example.fitnessapplicationhandheld.DestinationStats
import com.example.fitnessapplicationhandheld.DestinationToday
import com.example.fitnessapplicationhandheld.Destinations
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel

@Composable
fun FitnessHandheldApp(modifier: Modifier = Modifier,
                       viewModel: WorkoutViewModel) {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: DestinationToday.route


    val cardColors = CardColors(
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.inversePrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer
    )



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                //containerColor = MaterialTheme.colorScheme.inversePrimary,
                //contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Destinations.values.forEach { navDestination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = navDestination.icon,
                                contentDescription = null,
                            )
                        },
                        label = { Text(text = navDestination.route ) },
                        selected = currentRoute.startsWith(navDestination.route),
                        onClick = {
                            navController.navigate(navDestination.route)
                        },
                    )
                }
            }
        },
        topBar = {
            AnimatedVisibility(
                visible = true //PLACEHOLDER
            ) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = Destinations[currentRoute]?.topBarText ?: "ERROR",
                        fontSize = 25.sp, fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        floatingActionButton = {
            //tba
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DestinationToday.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = DestinationHistory.route) {
                HistoryScren(viewModel = viewModel)
            }
            composable(route = DestinationStats.route) {
                StatScreen(viewModel = viewModel)
            }

//            composable(
//                route = "${DestinationWorkout.route}/{workoutTimeStamp}",
//                arguments = listOf(navArgument("workoutTimeStamp") { type = NavType.LongType }),
//            ) { navBackStackEntry ->
//                val workoutTimeStamp = navBackStackEntry.arguments?.getLong("workoutTimeStamp") ?: 0
//                WorkoutScreen(
//                    viewModel = viewModel,
//                    workoutTimeStamp = workoutTimeStamp
//                )
//            }
            composable(
                route = DestinationToday.route
            ){
                TodaysActivityScreen(cardColors = cardColors, viewModel = viewModel)
            }


        }
    }
}