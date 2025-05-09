package com.example.fitnessapplicationhandheld

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CardColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.fitnessapplicationhandheld.screens.HistoryScreen
import com.example.fitnessapplicationhandheld.screens.NewWorkoutScreen
import com.example.fitnessapplicationhandheld.screens.StatScreen
import com.example.fitnessapplicationhandheld.screens.TodaysActivityScreen
import com.example.fitnessapplicationhandheld.screens.WorkoutDetailsScreen
import com.example.fitnessapplicationhandheld.screens.WorkoutLabelsScreen
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FitnessHandheldApp(modifier: Modifier = Modifier,
                       viewModel: WorkoutViewModel, path: String?) {
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


    val navigateToWorkoutDetails = { id: Long ->
        navController.myNavigate("${DestinationWorkoutDetails.route}/$id")
    }

    LaunchedEffect(Unit) {
        if (path == "/recent"){
            navigateToWorkoutDetails(0)
        }
    }

    val snackbarHostState = remember{ SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ) },
        bottomBar = {
            println(currentRoute)
            if (currentRoute.startsWith(DestinationWorkoutDetails.route) ||
                currentRoute == DestinationNewWorkoutLabel.route) return@Scaffold
            NavigationBar{
                bottomNavigationDestinations.forEach { navDestination ->
                    val selected = currentRoute.startsWith(navDestination.route)
                    NavigationBarItem(
                        icon = {
                            val iconSize = if (navDestination == DestinationToday) 40.dp
                                            else 30.dp
                            if (selected) navDestination.selectedIcon(iconSize) else navDestination.icon(iconSize)

                        },
                        label = {  },
                        selected = currentRoute.startsWith(navDestination.route),
                        onClick = {
                            navController.myNavigate(navDestination.route)
                        },
                    )
                }
            }
        },
        topBar = {
            AnimatedVisibility(
                visible = true //PLACEHOLDER
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Spacer(modifier = Modifier.fillMaxWidth()
                        .height(30.dp))
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        val segments = currentRoute.split("/")
                        println(segments[0])

                        if (segments[0] == DestinationWorkoutDetails.route ||
                            segments[0] == DestinationNewWorkoutLabel.route)
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    })
                        else Spacer(modifier = Modifier.size(20.dp))

                        Text(text = Destinations[segments[0]]?.topBarText ?: "ERROR",
                            fontSize = 25.sp, fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {

                            }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute != DestinationWorkoutLabels.route) return@Scaffold

            FloatingActionButton(
                onClick = {
                    navController.myNavigate(DestinationNewWorkoutLabel.route)
                },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(Icons.Filled.Add,null)
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (path == null || path == "/recent") DestinationToday.route
                                else if (path == "/daily") DestinationToday.route
                                else DestinationStats.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = DestinationHistory.route) {
                HistoryScreen(viewModel = viewModel, cardColors = cardColors,
                    onWorkoutClick = navigateToWorkoutDetails, snackbarState = snackbarHostState)
            }
            composable(route = DestinationStats.route) {
                StatScreen(viewModel = viewModel, cardColors = cardColors)
            }

            composable(
                route = "${DestinationWorkoutDetails.route}/{workoutTimeStamp}",
                arguments = listOf(navArgument("workoutTimeStamp") { type = NavType.LongType }),
            ) { navBackStackEntry ->
                val workoutTimeStamp = navBackStackEntry.arguments?.getLong("workoutTimeStamp") ?: 0
                WorkoutDetailsScreen(
                    id = workoutTimeStamp,
                    cardColors = cardColors,
                    viewModel = viewModel
                )
            }

            composable(
                route = DestinationToday.route
            ){
                TodaysActivityScreen(cardColors = cardColors, viewModel = viewModel, onWorkoutClick = navigateToWorkoutDetails)
            }

            composable(
                route = DestinationWorkoutLabels.route
            ) {
                WorkoutLabelsScreen(viewModel = viewModel, cardColors = cardColors)
            }


            composable(
                route = DestinationNewWorkoutLabel.route
            ) {
                NewWorkoutScreen(viewModel = viewModel, cardColors = cardColors, navigateBack = {navController.popBackStack()})
            }

        }
    }
}

fun NavController.myNavigate(route: String)=
    navigate(route){
        launchSingleTop = true
        restoreState = true
    }
