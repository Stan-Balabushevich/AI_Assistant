package id.slava.nt.chatgpthelper.domain.repository

import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.domain.model.BotResponse
import id.slava.nt.chatgpthelper.domain.model.UserRequest
import kotlinx.coroutines.flow.Flow

interface RequestRepository {

    suspend fun getChatGPTResponse(useSystemMessage: Boolean, chatgptModel: String, userMessages: List<UserRequest>): Flow<Resource<BotResponse>>

    suspend fun getGeminiResponse(useSystemMessage: Boolean, geminiModel: String, userMessages: List<UserRequest>): Flow<Resource<BotResponse>>

}