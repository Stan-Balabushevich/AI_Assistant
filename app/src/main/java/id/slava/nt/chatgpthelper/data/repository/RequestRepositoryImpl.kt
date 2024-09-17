package id.slava.nt.chatgpthelper.data.repository

import id.slava.nt.chatgpthelper.BuildConfig
import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.data.remote.OpenAIApiService
import id.slava.nt.chatgpthelper.data.remote.dt_object.ChatGPTRequest
import id.slava.nt.chatgpthelper.data.remote.dt_object.Message
import id.slava.nt.chatgpthelper.data.remote.dt_object.toGptResponse
import id.slava.nt.chatgpthelper.domain.model.GptResponse
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RequestRepositoryImpl(private val openAIApiService: OpenAIApiService) : RequestRepository {

    override suspend fun getChatGPTResponse(userMessage: String): Flow<Resource<GptResponse>> = flow {
        emit(Resource.Loading())

        val apiKey = BuildConfig.API_KEY
        val authHeader = "Bearer $apiKey"

        val messages = listOf(
            Message(role = "user", content = userMessage)
        )
        val request = ChatGPTRequest(messages = messages)

        try {
            val response = openAIApiService.getCompletion(authHeader, request)

            val gptResponse = response.toGptResponse()
            if (gptResponse.response != null) {
                emit(Resource.Success(gptResponse))
            } else {
                emit(Resource.Error("No response from GPT-4"))
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
