package id.slava.nt.chatgpthelper.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import id.slava.nt.chatgpthelper.BuildConfig
import id.slava.nt.chatgpthelper.common.GeminiModels
import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.data.remote.OpenAIApiService
import id.slava.nt.chatgpthelper.data.remote.dt_object.ChatGPTRequest
import id.slava.nt.chatgpthelper.data.remote.dt_object.Message
import id.slava.nt.chatgpthelper.data.remote.dt_object.toGptResponse
import id.slava.nt.chatgpthelper.data.remote.dt_object.toMessage
import id.slava.nt.chatgpthelper.domain.model.BotResponse
import id.slava.nt.chatgpthelper.domain.model.UserRequest
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RequestRepositoryImpl(private val openAIApiService: OpenAIApiService) : RequestRepository {

    override suspend fun getChatGPTResponse(
        useSystemMessage: Boolean,
        chatgptModel: String,
        userMessages: List<UserRequest>
    ): Flow<Resource<BotResponse>> = flow {
        emit(Resource.Loading())

        val apiKey = BuildConfig.API_KEY
        val authHeader = "Bearer $apiKey"

        val systemMessage = Message(
            role = "system",
            content = "You are a helpful and knowledgeable tutor specializing in Android Development and teaching Kotlin. " +
                    "Please note that sometimes I may make mistakes in writing some words, but all my questions are related to Android Development. " +
                    "Please try to understand what I mean even if there are small errors."
        )

        val messages = if (useSystemMessage) listOf(systemMessage) + userMessages.map { it.toMessage() } else userMessages.map { it.toMessage() }

        val request = ChatGPTRequest(model = chatgptModel, messages = messages)

        try {
            val response = openAIApiService.getCompletion(authHeader, request)

            val gptResponse = response.toGptResponse()
            if (gptResponse.response != null) {
                emit(Resource.Success(gptResponse))
            } else {
                emit(Resource.Error("No response from GPT"))
            }

        } catch (e: HttpException) {
            // Handle HTTP exceptions (like 404, 500, etc.)
            emit(Resource.Error("HTTP Error: ${e.code()} - ${e.message()}"))
        } catch (e: IOException) {
            // Handle network errors
            emit(Resource.Error("Network Error: ${e.message}"))
        } catch (e: Exception) {
            // Handle other exceptions
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }.catch { e ->
        emit(Resource.Error("Flow Exception: ${e.message}"))
    }

    override suspend fun getGeminiResponse(
        useSystemMessage: Boolean,
        geminiModel: String,
        userMessages: List<UserRequest>
    ): Flow<Resource<BotResponse>> = flow {

        emit(Resource.Loading())

        val generativeModel = GenerativeModel(
            modelName = geminiModel.ifEmpty { GeminiModels.GEMINI_1_5_FLASH.modelName },
            apiKey = BuildConfig.API_KEY_GEMINI
        )

        val systemMessage = Message(
            role = "system",
            content = "You are a helpful and knowledgeable tutor specializing in Android Development and teaching Kotlin. " +
                    "Please note that sometimes I may make mistakes in writing some words, but all my questions are related to Android Development. " +
                    "Please try to understand what I mean even if there are small errors."
        )

        val conversationWithSystem = if (useSystemMessage) listOf(systemMessage) + userMessages.map { it.toMessage() } else userMessages.map { it.toMessage() }

//        val conversationHistory = userMessages.joinToString(separator = "\n") {
//        if (it.role == "user") "User: ${it.content}" else "Bot: ${it.content}"
//    }

        // Format conversation history for API request
        val conversationHistory = conversationWithSystem.joinToString(separator = "\n") {
            when (it.role) {
                "user" -> "User: ${it.content}"
                "bot" -> "Bot: ${it.content}"
                "system" -> "System: ${it.content}"
                else -> it.content
            }
        }

        try {
            val response = generativeModel.generateContent(
                content {
                    text(conversationHistory)
                }
            )
            if (response.text != null) {
                val geminiResponse = BotResponse(response = response.text)
                emit(Resource.Success(geminiResponse))
            } else {
                emit(Resource.Error("No response from Gemini"))
            }

        } catch (e: HttpException) {
            // Handle HTTP exceptions (like 404, 500, etc.)
            emit(Resource.Error("HTTP Error: ${e.code()} - ${e.message()}"))
        } catch (e: IOException) {
            // Handle network errors
            emit(Resource.Error("Network Error: ${e.message}"))
        } catch (e: Exception) {
            // Handle other exceptions
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }.catch { e ->
        emit(Resource.Error("Flow Exception: ${e.message}"))
    }
}
