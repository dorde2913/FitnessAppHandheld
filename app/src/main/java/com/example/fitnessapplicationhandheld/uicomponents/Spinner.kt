package com.example.fitnessapplicationhandheld.uicomponents


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp


@Composable
fun Spinner(
    value: String,
    onSelect: (String) -> Unit,
    options: Map<String,Int>,
    modifier: Modifier = Modifier,
    cardColors: CardColors
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopEnd) {
        Column(){
            Row(
                modifier = Modifier
                    //.align(Alignment.Center)
                    .wrapContentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = value, color = cardColors.contentColor)
                //Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }

            // https://m2.material.io/components/menus/android
            // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary
            //Spacer(modifier = Modifier.weight(1f))
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                offset = DpOffset(0.dp, (-32).dp),
                modifier = Modifier.wrapContentSize(),
            ) {
                options.forEach { (label, color) ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ){
                                if (label!="All Gym Workouts" &&
                                    label != "All Cardio Workouts"){
                                    Box(
                                        modifier = Modifier.size(20.dp)
                                            .clip(CircleShape)
                                            .background(Color(color))
                                    )
                                }
                                Text(modifier = Modifier.padding(5.dp),text = label)
                            }
                        },
                        onClick = {
                            onSelect(label)
                            isExpanded = false
                        },
                    )
                }
            }
        }
    }
}