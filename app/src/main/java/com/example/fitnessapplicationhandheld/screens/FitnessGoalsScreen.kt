package com.example.fitnessapplicationhandheld.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnessapplicationhandheld.CALS_GOAL_KEY
import com.example.fitnessapplicationhandheld.CALS_KEY
import com.example.fitnessapplicationhandheld.R
import com.example.fitnessapplicationhandheld.STEPS_GOAL_KEY
import com.example.fitnessapplicationhandheld.STEPS_KEY
import com.example.fitnessapplicationhandheld.dataStore
import com.example.fitnessapplicationhandheld.uicomponents.darken
import kotlinx.coroutines.flow.map

@Composable
fun FitnessGoalsScreen(modifier: Modifier = Modifier,
                       cardColors: CardColors){
    val context = LocalContext.current

    val stepsGoal by context.dataStore.data.map{
        it[STEPS_GOAL_KEY]
    }.collectAsState(initial = 0)

    val goalCals by context.dataStore.data.map{
        it[CALS_GOAL_KEY]
    }.collectAsState(initial = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Card(
            colors = cardColors,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
                .height(150.dp)
        ){

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(cardColors.containerColor.darken(0.4f)),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(R.drawable.stepsicon),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = "Daily Steps Goal", fontSize = 20.sp)
                }

                var stepsTextField by rememberSaveable { mutableStateOf("$stepsGoal") }
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    TextField(
                        value = stepsTextField,
                        onValueChange = {stepsTextField = it},
                        colors = TextFieldDefaults.colors()
                            .copy(unfocusedContainerColor = cardColors.containerColor, focusedContainerColor = cardColors.containerColor)

                    )
                    //Text("$stepsGoal", fontSize = 30.sp, fontWeight = FontWeight.Medium)
                    Icon(Icons.Default.Create,null, modifier = Modifier.size(40.dp))
                }


            }

        }

    }
}