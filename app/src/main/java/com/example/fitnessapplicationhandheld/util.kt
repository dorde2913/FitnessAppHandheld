package com.example.fitnessapplicationhandheld

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun getComparison(first: Double, second: Double): String {
    if (first == 0.0 || second == 0.0 ||
        first == second) return "--"

    val sign = if (first > second)"+" else "-"

    return "$sign${ String.format("%.2f",100.0 - first / second) }%"
}