package com.example.fitnessapplicationhandheld

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessapplicationhandheld.services.WearMessageListenerService
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.ui.theme.FitnessApplicationHandheldTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passive_stats")

val STEPS_KEY = intPreferencesKey("daily_steps")
val STEPS_GOAL_KEY = intPreferencesKey("steps_goal")

val CALS_KEY = intPreferencesKey("daily_cals")
val CALS_GOAL_KEY = intPreferencesKey("cals_goal")



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        startService(
            Intent().apply{
                setClass(
                    this@MainActivity,
                    WearMessageListenerService::class.java
                )
            }
        )


        val path = this.intent.data?.path

        println(path)

        enableEdgeToEdge()
        setContent {
            FitnessApplicationHandheldTheme {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    context.dataStore.edit { preferences->
                        if (preferences[STEPS_GOAL_KEY] == null){
                            preferences[STEPS_GOAL_KEY] = 3000
                        }
                        if (preferences[CALS_GOAL_KEY] == null){
                            preferences[CALS_GOAL_KEY] = 1000
                        }
                    }
                }

                val viewModel: WorkoutViewModel = viewModel()
                FitnessHandheldApp(viewModel = viewModel, path = path)
            }
        }
    }
}



