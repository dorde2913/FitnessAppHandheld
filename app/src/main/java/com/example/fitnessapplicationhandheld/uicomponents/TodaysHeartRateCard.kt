package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent

@Composable
fun TodaysHeartRateCard(modifier: Modifier = Modifier, cardColors: CardColors){

    val dummyHR = listOf(
        77,60,64,65,47,88,90,95,
        77,60,64,65,47,88,90,95,
        77,60,64,65,47,88,90,95,
    )

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(Unit) {
        //println(HRMaxes.size)
        if (dummyHR.isNotEmpty()) {
            modelProducer.runTransaction {
                columnSeries {
                    series(y = dummyHR)
                }
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = cardColors
    ) {
        Text("Today's Peak Heart Rates", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            fill = Fill(color = Color.Red.toArgb()),
                            thickness = 10.dp
                        )
                    )
                ),
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
                        "${value.toInt()}h"
                    },
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

                ),
            zoomState = rememberVicoZoomState(
                initialZoom = Zoom.x(dummyHR.size.toDouble())
            ),
            modelProducer = modelProducer,
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
        )
    }
}