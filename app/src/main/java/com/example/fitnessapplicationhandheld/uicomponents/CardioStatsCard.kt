package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.database.models.Location
import com.example.fitnessapplicationhandheld.screens.BasicStatRow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.ktx.utils.sphericalDistance

@Composable
fun CardioStatsCard(
    locationList: List<Location>,
    cardColors: CardColors,
    distance: String,
    averageSpeed: String,
    distanceComparison: String,
    speedComparison: String
){


    val route = locationList.map{coords-> LatLng(coords.latitude,coords.longitude) }


    val actualRoutes: MutableList<MutableList<LatLng>> = mutableListOf()
    var n = 0
    for (i in route.indices){
        if (actualRoutes.size == n){
            actualRoutes.add(mutableListOf())
        }

        actualRoutes[n].add(route[i])
        if (i < route.size - 1){
            if (route[i].sphericalDistance(route[i+1]) >= 50.0){
                n++
            }
        }


    }

    val cameraPositionState = rememberCameraPositionState()

    var isMapLoaded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(horizontal = 10.dp)
            .fillMaxWidth().heightIn(min = 100.dp),
        colors = cardColors
    ){
        if (locationList.isEmpty())
        {
            Spacer(modifier = Modifier.height(40.dp))
            Text("No data :(", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
        else
            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text("Workout Route, ${locationList.size}", fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 10.dp))
                }


                GoogleMap(
                    cameraPositionState = cameraPositionState,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    onMapLoaded = {isMapLoaded = true
                        val boundsBuilder = LatLngBounds.Builder()
                        route.forEach { boundsBuilder.include(it) }
                        val bounds = boundsBuilder.build()
                        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                    },
//                    uiSettings = MapUiSettings(
//                        scrollGesturesEnabled = false,
//                        scrollGesturesEnabledDuringRotateOrZoom = false
//                    )

                ){

                    for (actualRoute in actualRoutes){
                        println("line: ${actualRoute.size}")
                        Polyline(
                            points = actualRoute,
                            color =  Color.Red,
                        )
                    }


                }
                BasicStatRow(
                    icon = R.drawable.distanceicon_removebg_preview,
                    iconSize = 40,
                    label = "Distance covered:",
                    value = distance,
                    diff = distanceComparison,
                    cardColors = cardColors
                )
                BasicStatRow(
                    icon = R.drawable.speedicon_removebg_preview,
                    iconSize = 40,
                    label = "Average speed:",
                    value = averageSpeed,
                    diff = speedComparison,
                    cardColors = cardColors
                )

            }




    }





}