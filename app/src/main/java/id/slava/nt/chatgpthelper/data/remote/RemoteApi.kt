package id.slava.nt.chatgpthelper.data.remote

import id.slava.nt.chatgpthelper.data.remote.dt_object.ChatGPTRequest
import id.slava.nt.chatgpthelper.data.remote.dt_object.ChatGPTResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

const val BASE_URL = "https://api.openai.com/"

interface OpenAIApiService {
    @POST("v1/chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: ChatGPTRequest
    ): ChatGPTResponse
}