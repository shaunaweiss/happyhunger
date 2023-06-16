package src.client

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ChatGptClient(private val apiKey: String) {
    private val client = OkHttpClient()
    private val mediaType = "application/json".toMediaType()

    fun generateMealSuggestion(prompt: String): String {
        val jsonObject = JSONObject()
            .put("prompt", prompt)
            .put("max_tokens", 50)

        val requestBody = jsonObject.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/davinci-codex/completions")
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
