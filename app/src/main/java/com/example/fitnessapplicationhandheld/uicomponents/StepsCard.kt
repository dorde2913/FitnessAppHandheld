package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.example.fitnessapplicationhandheld.CALS_KEY
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.STEPS_KEY
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import kotlinx.coroutines.flow.map

@Composable
fun StepsCard(modifier: Modifier = Modifier,
              stepsGoal: Int,
              goalCals: Int,
              cardColors: CardColors,
              viewModel: WorkoutViewModel){

    val context = LocalContext.current

    val dailyCals by context.dataStore.data.map{
        it[CALS_KEY]
    }.collectAsState(initial = 0)

    val dailySteps by context.dataStore.data.map{
        it[STEPS_KEY]
    }.collectAsState(initial = 0)


    println(dailySteps)

    Card(modifier = modifier,
        colors = cardColors
        ){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround){

            CircularIndicatorColumn(
                label = "Steps Walked",
                icon = R.drawable.stepsicon,
                currentValue = dailySteps?:0,
                goalValue = stepsGoal,
                cardColors = cardColors
            )

            CircularIndicatorColumn(
                label = "Calories Burned",
                icon = R.drawable.caloriesicon_removebg_preview,
                currentValue = dailyCals?:0,
                goalValue = goalCals,
                cardColors = cardColors
            )


        }


    }

}
@Composable
fun CircularIndicatorColumn(
    label: String,
    icon: Int,
    currentValue: Int,
    goalValue: Int,
    cardColors: CardColors,
    ){

    var progress by remember{ mutableStateOf(0f) }

    val stepsProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(currentValue) {
        progress = currentValue/goalValue.toFloat()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(label)

        Box(modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.Center){
            Box(
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){
                    Icon(painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(50.dp))
                    Text("$currentValue/$goalValue", textAlign = TextAlign.Center,
                        fontSize = 20.sp, fontWeight = FontWeight.Medium)
                }

            }
            CircularProgressIndicator(
                modifier = Modifier.size(150.dp),
                progress = {
                    stepsProgress.value
                },
                color =
                if (currentValue < goalValue) cardColors.contentColor
                else Color.Green,
                strokeWidth = 7.dp,
                gapSize = 2.dp,
                strokeCap = StrokeCap.Square,
                trackColor = cardColors.disabledContentColor
            )
        }
    }
}


fun Color.lighten(factor: Float): Color {
    val newColor = ColorUtils.blendARGB(this.toArgb(), Color.White.toArgb(), factor)
    return Color(newColor)
}
fun Color.darken(factor: Float): Color {
    val newColor = ColorUtils.blendARGB(this.toArgb(), Color.Black.toArgb(), factor)
    return Color(newColor)
}

