@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitnessapplicationhandheld.uicomponents

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.datastore.preferences.core.edit
import com.example.fitnessapplicationhandheld.CALS_GOAL_KEY
import com.example.fitnessapplicationhandheld.CALS_KEY
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.STEPS_GOAL_KEY
import com.example.fitnessapplicationhandheld.STEPS_KEY
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@Composable
fun StepsCard(modifier: Modifier = Modifier,
              cardColors: CardColors,
              viewModel: WorkoutViewModel){



    val context = LocalContext.current

    val dailyCals by context.dataStore.data.map{
        it[CALS_KEY]
    }.collectAsState(initial = 0)

    val dailySteps by context.dataStore.data.map{
        it[STEPS_KEY]
    }.collectAsState(initial = 0)


    val stepsGoal by context.dataStore.data.map{
        it[STEPS_GOAL_KEY]
    }.collectAsState(initial = 0)

    val goalCals by context.dataStore.data.map{
        it[CALS_GOAL_KEY]
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
                goalValue = stepsGoal?:0,
                cardColors = cardColors,
                viewModel = viewModel
            )

            CircularIndicatorColumn(
                label = "Calories Burned",
                icon = R.drawable.caloriesicon_removebg_preview,
                currentValue = dailyCals?:0,
                goalValue = goalCals?:0,
                cardColors = cardColors,
                viewModel = viewModel
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
    viewModel: WorkoutViewModel
    ){

    val context = LocalContext.current
    var progress by remember{ mutableStateOf(0f) }

    val stepsProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    LaunchedEffect(currentValue,goalValue) {
        if (goalValue == 0) progress = 0f
        else progress = currentValue/goalValue.toFloat()
    }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog){
        BasicAlertDialog(
            onDismissRequest = {showDialog = false},
        ){
            var textFieldValue by rememberSaveable { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .background(cardColors.containerColor, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Edit $label Goal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cardColors.contentColor)
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = textFieldValue,
                        onValueChange = {
                            if (it.toIntOrNull() != null || it == "")textFieldValue = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Button(onClick = { showDialog = false },
                            ) {
                            Text("Close")
                        }
                        Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    context.dataStore.edit { preferences->
                                       if (label == "Steps Walked"){
                                           preferences[STEPS_GOAL_KEY] = textFieldValue.toInt()
                                           viewModel.sendStepsGoal(textFieldValue.toInt())
                                       }
                                       else{
                                           preferences[CALS_GOAL_KEY] = textFieldValue.toInt()
                                           viewModel.sendCalsGoal(textFieldValue.toInt())
                                       }
                                    }
                                    showDialog = false
                                }
                            },
                            enabled = textFieldValue!="") {
                            Text("Save")
                        }
                    }

                }
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(label)

        Box(modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.Center){
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .matchParentSize()
                    .clickable {
                        showDialog = true
                    },
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

