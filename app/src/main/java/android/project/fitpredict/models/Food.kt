package android.project.fitpredict.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val foodName: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fats: Double = 0.0,
    val loggedBy: String = "",
    val loggedDate: String = "",
    val loggedTime: String = ""
): Parcelable
