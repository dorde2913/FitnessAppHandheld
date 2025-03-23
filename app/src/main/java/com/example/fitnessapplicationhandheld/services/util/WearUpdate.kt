package com.example.fitnessapplicationhandheld.services.util

import kotlinx.serialization.Serializable

interface WearUpdate

@Serializable
data class BPMUpdate(
    val id: Long = 0,
    val label: String = "",
    val length: Long = 0,
    val BPMList: List<Int> = listOf()
): WearUpdate

@Serializable
data class LocationUpdate(
    val id: Long = 0,
    val label: String = "",
    val locationList: List<Pair<Double,Double>> = listOf()
): WearUpdate

@Serializable
data class CaloriesUpdate(
    val id: Long = 0,
    val label: String = "",
    val calories: Int = 0
): WearUpdate

@Serializable
data class DistanceUpdate(
    val id: Long = 0,
    val label: String = "",
    val distance: Int = 0
): WearUpdate

@Serializable
data class DailyStepsUpdate(
    val steps: Int = 0
): WearUpdate

@Serializable
data class DailyCaloriesUpdate(
    val calories: Int = 0
): WearUpdate

@Serializable
data class DailyHeartRateUpdate(
    val hrList: List<Int> = listOf()
): WearUpdate