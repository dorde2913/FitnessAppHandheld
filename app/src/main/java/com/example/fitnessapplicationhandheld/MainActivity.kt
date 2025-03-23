package com.example.fitnessapplicationhandheld

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnessapplicationhandheld.services.WearMessageListenerService
import com.example.fitnessapplicationhandheld.stateholders.WorkoutViewModel
import com.example.fitnessapplicationhandheld.ui.theme.FitnessApplicationHandheldTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passive_stats")

val STEPS_KEY = intPreferencesKey("daily_steps")
val CALS_KEY = intPreferencesKey("daily_cals")

val DAILY_HR_KEY = byteArrayPreferencesKey("daily_hr")

//context.dataStore.edit { preferences->
//    if (preferences[MIN_KEY] == null){
//        preferences[MIN_KEY] = heartrate
//    }
//    if (preferences[MAX_KEY] == null){
//        preferences[MAX_KEY] = heartrate
//    }
//}
//
//val min by context.dataStore.data
//    .map{
//        it[MIN_KEY]
//    }.collectAsState(initial = 0)

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



        enableEdgeToEdge()
        setContent {
            FitnessApplicationHandheldTheme {
                val viewModel: WorkoutViewModel = viewModel()
                FitnessHandheldApp(viewModel = viewModel)
            }
        }
    }
}



