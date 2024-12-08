package android.project.fitpredict.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMetric(
    val age: String,
    val gender: String,
    val height: String, // in cm
    val weight: String, // in kg
    val activityLevel: String,
    val totalCalories: String,
    val totalProtein: String,
    val totalCarbs: String,
    val totalFats: String
) : Parcelable
