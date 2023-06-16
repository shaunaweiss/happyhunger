package happyhunger.client 

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ChatGptClient(private val apiUrl: String) {
    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()

    val apiKey = System.getenv("CHATGPT_API_TOKEN")


    fun generateMealSuggestion(prompt: String): String {
        val jsonObject = JSONObject()
            .put("prompt", prompt)
            .put("max_tokens", 50)

        val requestBody = jsonObject.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        val jsonObjectResponse = JSONObject(responseBody)
        val choices = jsonObjectResponse.getJSONArray("choices")
        val suggestion = choices.getJSONObject(0).getString("text")

        return suggestion
    }
}
