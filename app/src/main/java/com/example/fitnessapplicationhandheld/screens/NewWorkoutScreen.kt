package com.example.fitnessapplicationhandheld.screens


import android.widget.Toast
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapplicationhandheld.database.models.Workout
import com.example.fitnessapplicationhandheld.database.models.WorkoutLabel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.uicomponents.HueBar
import com.example.fitnessapplicationhandheld.uicomponents.SatValPanel
import com.example.fitnessapplicationhandheld.uicomponents.workout.WorkoutLabelIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NewWorkoutScreen(modifier: Modifier = Modifier,
                     viewModel: WorkoutViewModel,
                     cardColors: CardColors, navigateBack: ()->Unit){

    val context = LocalContext.current
    val workoutLabels by viewModel.labels.collectAsState(initial = listOf())

    var gymColor by rememberSaveable { mutableStateOf(0) }
    var cardioColor by rememberSaveable { mutableStateOf(0)}

    //bolje radio dugme prakticno da se napravi
    var selected by rememberSaveable { mutableStateOf(false) }


    val hsv = remember {
        val hsv = floatArrayOf(0f, 0f, 0f)
        android.graphics.Color.colorToHSV(Color.Blue.toArgb(), hsv)

        mutableStateOf(
            Triple(hsv[0], hsv[1], hsv[2])
        )
    }
    val workoutColor = remember(hsv.value) {
        mutableStateOf(Color.hsv(hsv.value.first, hsv.value.second, hsv.value.third))
    }

    var showDropDown by rememberSaveable { mutableStateOf(false) }


    var workoutName by rememberSaveable { mutableStateOf("") }
    var labels = workoutLabels.map{it.label}.toSet()
    LaunchedEffect(selected, workoutColor.value,workoutLabels) {
        if (selected) {
            gymColor = workoutColor.value.toArgb()
            cardioColor = 0
        }
        else{
            cardioColor = workoutColor.value.toArgb()
            gymColor = 0
        }

        labels = workoutLabels.map{it.label}.toSet()
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                WorkoutLabelIcon(
                    onClick = {selected = true},
                    workoutType = WorkoutType.GYM,
                    color = gymColor,
                    iconTint = cardColors.contentColor, boxSize = 120)

                if (selected)
                    Text("GYM"
                    , textAlign = TextAlign.Center, fontSize = 20.sp)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WorkoutLabelIcon(
                    onClick = {selected = false},
                    workoutType = WorkoutType.CARDIO,
                    color = cardioColor,
                    iconTint = cardColors.contentColor, boxSize = 120)
                if (!selected)
                    Text("CARDIO"
                        , textAlign = TextAlign.Center, fontSize = 20.sp)
            }

        }



        Column(
            modifier = Modifier
                .clickable{
                    showDropDown = true
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.White)
                    .padding(3.dp)
                    .background(workoutColor.value)
            )
            Icon(Icons.Default.ArrowDropDown,"contentdesc",modifier = Modifier.size(30.dp))
            DropdownMenu(
                expanded = showDropDown,
                onDismissRequest = {
                    showDropDown = false
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    SatValPanel(hue = hsv.value.first) { sat, value ->
                        hsv.value = Triple(hsv.value.first, sat, value)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    HueBar { hue ->
                        hsv.value = Triple(hue, hsv.value.second, hsv.value.third)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        var isError by remember{ mutableStateOf(false) }

        OutlinedTextField(
            value = workoutName,
            onValueChange = {
                workoutName = it
                isError = labels.contains(workoutName)
                //ovde treba i provera originalnosti imena
            },
            //placeholder = {Text(text = "New Workout Name")},
            leadingIcon = {Icon(Icons.Filled.Create,null)},
            label = {Text(text = "New Workout Name")},
            isError = isError,
            supportingText = {if (isError)Text(text = "Workout names should be unique!")}
        )
        val snackbarHostState = remember { SnackbarHostState() }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            modifier = Modifier.height(50.dp),
            enabled = !isError && workoutName!="",
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertWorkoutLabel(
                        WorkoutLabel(label = workoutName,
                            workoutType = if (selected) WorkoutType.GYM else WorkoutType.CARDIO,
                            color = workoutColor.value.toArgb())
                    )
                }
                Toast.makeText(
                    context,
                    "Workout $workoutName Successfully Created!",
                    Toast.LENGTH_SHORT
                ).show()
                navigateBack()
            }
        ){
            Text(text = "Save Workout", fontSize = 20.sp)
        }


    }

}