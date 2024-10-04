package android.project.fitpredict.models

data class FoodItem(
    val food_name: String,
    val serving_qty: Double,
    val serving_unit: String,
    val nf_calories: Double,
    val nf_protein: Double,
    val nf_total_carbohydrate: Double,
    val nf_total_fat: Double
)