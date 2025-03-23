package com.example.fitnessapplicationhandheld.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fitnessapplicationhandheld.database.models.WorkoutType
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
): ViewModel() {

    val workouts = repository.workouts
    val labels = repository.labels


    val dailyHR = repository.dailyHR

    fun getHRList(parentId: Long) =
        repository.getHRList(parentId)

    fun getAverageBPM(parentId: Long) =
        repository.getAverageBPM(parentId)

    fun getCardioWorkouts() =
        repository.getCardioWorkouts()

    fun getRoute(id: Long) =
        repository.getRoute(id)

    fun getWorkout(id: Long) =
        repository.getWorkout(id)

    fun getAverageLengthByLabel(label: String) =
        repository.getAverageLengthByLabel(label)

    fun getAverageLengthByType(type: WorkoutType) =
        repository.getAverageLengthByType(type)

    fun getAverageCaloriesByLabel(label: String) =
        repository.getAverageCaloriesByLabel(label)

    fun getAverageCaloriesByType(type: WorkoutType) =
        repository.getAverageCaloriesByType(type)

    fun getAverageBPMByType(type: WorkoutType) =
        repository.getAverageBPMByType(type)

    fun getAverageBPMByLabel(label: String) =
        repository.getAverageBPMByLabel(label)

    fun getLabelsByType(type: WorkoutType) =
        repository.getLabelsByType(type)

    //cardio
    fun getAverageDistance()=
        repository.getAverageDistance()


    fun getAverageDistanceByLabel(label: String) =
        repository.getAverageDistanceByLabel(label = label)


    fun getAverageSpeed() =
        repository.getAverageSpeed()


    fun getAverageSpeedByLabel( label: String) =
        repository.getAverageSpeedByLabel(label = label)

}