package id.slava.nt.chatgpthelper.domain.repository

import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.domain.model.BotResponse
import id.slava.nt.chatgpthelper.domain.model.UserRequest
import kotlinx.coroutines.flow.Flow

interface RequestRepository {

    suspend fun getChatGPTResponse(gptModel: String, userMessages: List<UserRequest>): Flow<Resource<BotResponse>>

    suspend fun getGeminiResponse(geminiModel: String, userMessages: List<UserRequest>): Flow<Resource<BotResponse>>

}