package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.example.fitnessapplicationhandheld.database.models.HRList
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.CartesianChart
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BPMGraphCard(
    modifier: Modifier = Modifier,
    bpmList: List<HRList>,
    cardColors: CardColors
){
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")


    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(bpmList) {
        if (bpmList.isNotEmpty())
            modelProducer.runTransaction {
                lineSeries { series(bpmList.map{it.value}) }
            }
    }



    Card(
        modifier = modifier.fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(horizontal = 10.dp),
        colors = cardColors
    ){
        if (bpmList.isEmpty()){
            Spacer(modifier = Modifier.height(40.dp))
            Text("No data :(",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        else{
            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(
                        lineProvider = LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.Line(
                                fill = LineCartesianLayer.LineFill.single(
                                    fill = Fill(Color.Green.toArgb())
                                )
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
                            //println(value)
                            LocalTime.ofNanoOfDay(value.toInt() * 1_000_000_000L).format(formatter)

                            //value.toInt().toString()
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
                initialZoom = Zoom.x(bpmList.size.toDouble())
            ),
                modelProducer = modelProducer,
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
            )
        }




    }


}