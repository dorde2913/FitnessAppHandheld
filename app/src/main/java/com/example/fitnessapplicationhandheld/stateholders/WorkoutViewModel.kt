package com.example.fitnessapplicationhandheld.stateholders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.fitnessapplicationhandheld.repositories.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
): ViewModel() {

    val workouts = repository.workouts


    fun getHRList(parentId: Long) =
        repository.getHRList(parentId)

    fun getAverageBPM(parentId: Long) =
        repository.getAverageBPM(parentId)

}