package android.project.fitpredict.models

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(
    val food_name: String = "",
    var serving_qty: Double = 0.0,
    val serving_unit: String = "",
    var nf_calories: Double = 0.0,
    val nf_protein: Double = 0.0,
    val nf_total_carbohydrate: Double = 0.0,
    val nf_total_fat: Double = 0.0,
    val photo: Photo?
): Parcelable