package com.example.gemmamessenger.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class ModelManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val agentContexts = ConcurrentHashMap<String, MutableList<Message>>()

    companion object {
        private const val TAG = "ModelManager"
        private const val API_KEY = "AIzaSyDA3amxFAKM17sKxSS77dB5-Gi1tZpoTjc"
        private const val PROJECT_NUMBER = "723631307795"
        private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-3-1b-it-int4:generateContent?key=$API_KEY"
        private const val MAX_CONTEXT_MESSAGES = 10
    }

    suspend fun initializeModel(): Boolean = withContext(Dispatchers.IO) {
        try {
            // Test API connection
            val testRequest = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models?key=$API_KEY")
                .build()

            val response = client.newCall(testRequest).execute()
            val success = response.isSuccessful

            if (!success) {
                Log.e(TAG, "API connection test failed: ${response.code} ${response.message}")
            }

            response.close()
            success
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing API connection", e)
            false
        }
    }

    suspend fun generateResponse(
        userMessage: String,
        agent: Agent
    ): String = withContext(Dispatchers.IO) {
        try {
            // Get or create context for this agent
            val context = agentContexts.getOrPut(agent.id) { mutableListOf() }

            // Add user message to context
            val newUserMessage = Message(
                content = userMessage,
                isFromUser = true
            )
            context.add(newUserMessage)

            // Create request JSON
            val requestJson = createRequestJson(agent, context)

            // Make API request
            val requestBody = requestJson.toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e(TAG, "API request failed: ${response.code} ${response.message}")
                throw IOException("API request failed: ${response.code} ${response.message}")
            }

            val responseBody = response.body?.string() ?: throw IOException("Empty response body")
            val jsonResponse = JSONObject(responseBody)

            // Parse response
            val content = jsonResponse.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")

            val parts = content.getJSONArray("parts")
            val responseText = parts.getJSONObject(0).getString("text")

            // Add agent response to context
            val agentMessage = Message(
                content = responseText,
                isFromUser = false,
                agentId = agent.id,
                agentName = agent.name
            )
            context.add(agentMessage)

            // Trim context if needed
            if (context.size > MAX_CONTEXT_MESSAGES) {
                val toRemove = context.size - MAX_CONTEXT_MESSAGES
                context.subList(0, toRemove).clear()
            }

            responseText
        } catch (e: Exception) {
            Log.e(TAG, "Error generating response", e)
            "Sorry, I encountered an error while processing your request: ${e.message}"
        }
    }

    private fun createRequestJson(agent: Agent, messages: List<Message>): JSONObject {
        val jsonObject = JSONObject()
        val contents = JSONArray()

        // Add system prompt as first message
        val systemContent = JSONObject()
        systemContent.put("role", "system")
        val systemParts = JSONArray()
        systemParts.put(JSONObject().put("text", agent.systemPrompt))
        systemContent.put("parts", systemParts)
        contents.put(systemContent)

        // Add conversation history
        for (message in messages) {
            val messageContent = JSONObject()
            messageContent.put("role", if (message.isFromUser) "user" else "model")

            val messageParts = JSONArray()
            messageParts.put(JSONObject().put("text", message.content))
            messageContent.put("parts", messageParts)

            contents.put(messageContent)
        }

        jsonObject.put("contents", contents)
        jsonObject.put("generationConfig", JSONObject()
            .put("temperature", 0.7)
            .put("maxOutputTokens", 1024)
            .put("topP", 0.95)
            .put("topK", 40)
        )

        return jsonObject
    }

    fun clearContext(agentId: String) {
        agentContexts.remove(agentId)
    }

    fun clearAllContexts() {
        agentContexts.clear()
    }

    fun close() {
        // No need to close anything with the API approach
    }
}
