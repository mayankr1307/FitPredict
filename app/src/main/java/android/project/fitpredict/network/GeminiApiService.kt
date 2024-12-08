package android.project.fitpredict.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

// Define the API interface
interface GeminiApiService {

    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent")
    fun generateContent(
        @Query("key") apiKey: String,   // Pass API key as query parameter
        @Body request: GeminiRequest
    ): Call<GeminiResponse>
}


// Define the response data class
data class GeminiResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)
