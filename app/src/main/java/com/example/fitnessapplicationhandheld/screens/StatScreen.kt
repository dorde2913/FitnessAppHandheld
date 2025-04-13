package com.example.fitnessapplicationhandheld.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.formatLength
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.Spinner
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.HorizontalLegend
import com.patrykandpatrick.vico.core.common.Legend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.Shape
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@Composable
fun StatScreen(modifier: Modifier = Modifier, viewModel: WorkoutViewModel, cardColors: CardColors){


    var selected by remember{ mutableStateOf(WorkoutType.GYM) }
    val workouts by viewModel.getWorkoutsByType(selected).collectAsState(initial = listOf())

    val labels by viewModel.getOrderedLabelsByType(selected).collectAsState(initial = listOf())

    /*
    ovde treba da se bira gym/cardio
     */

    val averageLength by viewModel.getAverageLengthByType(selected).collectAsState(initial = 0.0)
    val averageCals by viewModel.getAverageCaloriesByType(selected).collectAsState(initial = 0.0)
    val averageBPM by viewModel.getAverageBPMByType(selected).collectAsState(initial = 0.0)

    val averageDistance by viewModel.getAverageDistance().collectAsState(initial = 0)
    val averageSpeed by viewModel.getAverageSpeed().collectAsState(initial = 0)



    val modelProducer = remember { CartesianChartModelProducer()}

    val cardioModelProducer = remember{ CartesianChartModelProducer()}





    LaunchedEffect(workouts,labels) {
        //println(HRMaxes.size)
        if (workouts.isNotEmpty() && labels.isNotEmpty()) {
            modelProducer.runTransaction {
                columnSeries {
                    for (label in labels){
                        val tempList: MutableList<Int> = mutableListOf()
                        val calsList: MutableList<Int> = mutableListOf()

                        workouts.forEachIndexed{ index, workout ->
                            if (workout.label == label.label){
                                tempList.add(index)
                                calsList.add(workout.calories)
                            }
                        }
                        //println(label.label)
                        //println("cals list: ${calsList}, x list: $tempList")

                        if (calsList.isNotEmpty())
                            series( y = calsList,
                                x = tempList)
                    }
                    series(0)
                }
            }

            cardioModelProducer.runTransaction {
                columnSeries {
                    for (label in labels){
                        if (label.workoutType == WorkoutType.GYM) continue
                        val tempList: MutableList<Int> = mutableListOf()
                        val calsList: MutableList<Int> = mutableListOf()

                        workouts.forEachIndexed{ index, workout ->
                            if (workout.label == label.label){
                                tempList.add(index)
                                calsList.add(workout.distance)
                            }
                        }
                        println(label.label)
                        println("distance list: ${calsList}, x list: $tempList")

                        if (calsList.isNotEmpty())
                            series( y = calsList,
                                x = tempList)
                    }
                    series(0)
                }
            }


        }
    }





    val columnComponents =
        if (labels.isNotEmpty()){
            labels.map{
                rememberLineComponent(
                    fill = Fill(it.color), thickness = 30.dp
                )
            }
        }
        else {
            listOf(rememberLineComponent(
                fill = Fill(Color.Blue.toArgb())
            ))
        }



    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                state = rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End){
            Spinner(
                value = selected,
                onSelect = {selected = it},
                options = listOf(WorkoutType.GYM,WorkoutType.CARDIO),
                cardColors = cardColors
            )
        }

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = cardColors
        ) {
            Text(
                "Calories burned per workout",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            if (workouts.isNotEmpty() && labels.isNotEmpty()){

                val legend:  Legend<CartesianMeasuringContext, CartesianDrawingContext> =
                    HorizontalLegend(
                        items = {
                            labels.forEach{
                                println("added legend item")
                                this.add(
                                    LegendItem(
                                        icon = ShapeComponent(shape = Shape.Rectangle, fill = Fill(it.color)),
                                        label = it.label,
                                        labelComponent = TextComponent(color = cardColors.contentColor.toArgb())
                                    )
                                )

                            }
                        }
                    )
//                    rememberHorizontalLegend(
//
//                    )

                CartesianChartHost(
                    rememberCartesianChart(
                        rememberColumnCartesianLayer(
                            columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                columnComponents
                            ),
                        )
                        ,
                        startAxis = VerticalAxis.rememberStart(
                            line = LineComponent(
                                fill = Fill(cardColors.contentColor.toArgb()),
                            ),
                            label = rememberAxisLabelComponent(
                                color = cardColors.contentColor,
                            ),
                            guideline = rememberLineComponent(
                                thickness = 0.dp
                            ),
                            tick = rememberAxisTickComponent(
                                fill = Fill(cardColors.contentColor.toArgb())
                            )
                        ),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            line = LineComponent(
                                fill = Fill(cardColors.contentColor.toArgb()),
                            ),
                            valueFormatter = { context, value, verticalAxisPosition ->
                                val index = value.toInt()
                                val dateFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")

                                if (index < workouts.size)
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(workouts[index].timestamp),
                                        ZoneId.systemDefault()).format(dateFormatter)
                                else "error"
                            },
                            label = rememberAxisLabelComponent(
                                color = cardColors.contentColor,
                            ),
                            guideline = rememberLineComponent(
                                thickness = 0.dp
                            ),
                            tick = rememberAxisTickComponent(
                                fill = Fill(cardColors.contentColor.toArgb())
                            ),
                        ),
                        legend = legend


                    ),

                    zoomState = rememberVicoZoomState(
                        initialZoom = Zoom.x(workouts.size.toDouble())
                    ),
                    modelProducer = modelProducer,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                )
            }
            else Text("NO DATA :(")

        }
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = cardColors
        ) {

            BasicStatRow(
                icon = R.drawable.stopwatchicon_removebg_preview,
                iconSize = 50,
                value = formatLength(averageLength.toLong()),
                label = "Average Workout Length",
                cardColors = cardColors
            )
            BasicStatRow(
                icon = R.drawable.caloriesicon_removebg_preview,
                iconSize = 40,
                value = averageCals.toInt().toString(),
                label = "Average Calories Burned(t?)",
                cardColors = cardColors
            )
            BasicStatRow(
                icon = R.drawable.heartrateicon_removebg_preview,
                iconSize = 25,
                value = averageBPM.toInt().toString(),
                label = "Average Workout BPM",
                cardColors = cardColors
            )


        }


        if (selected != WorkoutType.CARDIO) return@Column


        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = cardColors
        ) {
            Text(
                "Distance crossed per workout",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (workouts.isNotEmpty() && labels.isNotEmpty()){
                CartesianChartHost(
                    rememberCartesianChart(
                        rememberColumnCartesianLayer(
                            columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                columnComponents
                            ),
                        )
                        ,
                        startAxis = VerticalAxis.rememberStart(
                            line = LineComponent(
                                fill = Fill(cardColors.contentColor.toArgb()),
                            ),
                            label = rememberAxisLabelComponent(
                                color = cardColors.contentColor,
                            ),
                            guideline = rememberLineComponent(
                                thickness = 0.dp
                            ),
                            tick = rememberAxisTickComponent(
                                fill = Fill(cardColors.contentColor.toArgb())
                            )
                        ),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            line = LineComponent(
                                fill = Fill(cardColors.contentColor.toArgb()),
                            ),
                            valueFormatter = { context, value, verticalAxisPosition ->
                                val index = value.toInt()
                                val dateFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")

                                if (index < workouts.size)
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochSecond(workouts[index].timestamp),
                                        ZoneId.systemDefault()).format(dateFormatter)
                                else "error"
                            },
                            label = rememberAxisLabelComponent(
                                color = cardColors.contentColor,
                            ),
                            guideline = rememberLineComponent(
                                thickness = 0.dp
                            ),
                            tick = rememberAxisTickComponent(
                                fill = Fill(cardColors.contentColor.toArgb())
                            ),
                        ),
                        legend = rememberHorizontalLegend(
                            items = {

                                labels.forEach {
                                    this.add(
                                        LegendItem(
                                            label = it.label,
                                            labelComponent = TextComponent(color = cardColors.contentColor.toArgb()),
                                            icon = ShapeComponent(shape = Shape.Rectangle, fill = Fill(it.color))
                                        )
                                    )

                                }

                            }
                        )

                        ),

                    zoomState = rememberVicoZoomState(
                        initialZoom = Zoom.x(workouts.size.toDouble())
                    ),
                    modelProducer = cardioModelProducer,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                )
            }
            else Text("NO DATA :(")

        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = cardColors
        ) {

            BasicStatRow(
                icon = R.drawable.distanceicon_removebg_preview,
                iconSize = 30,
                value = "${averageDistance.toInt()}m",
                label = "Average Distance",
                cardColors = cardColors
            )
            BasicStatRow(
                icon = R.drawable.speedicon_removebg_preview,
                iconSize = 35,
                value = "${String.format(" % .2f",averageSpeed)}m/s",
                label = "Average Speed",
                cardColors = cardColors
            )

        }



    }
}



