package id.slava.nt.chatgpthelper.domain.repository

import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.domain.model.GptResponse
import kotlinx.coroutines.flow.Flow

interface RequestRepository {

    suspend fun getChatGPTResponse(userMessage: String): Flow<Resource<GptResponse>>

}