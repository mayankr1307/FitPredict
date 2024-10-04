package android.project.fitpredict.nutritionix

import android.project.fitpredict.models.FoodItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class FoodQuery(val query: String)

data class FoodResponse(
    val foods: List<FoodItem>
)

interface NutritionixApiService {
    @Headers(
        "Content-Type: application/json",
        "x-app-id: db998909",
        "x-app-key: d21e271e3827e2fb2ddcc1d08c4db17e"
    )
    @POST("/v2/natural/nutrients")
    suspend fun searchFood(@Body query: FoodQuery): Response<FoodResponse>
}
