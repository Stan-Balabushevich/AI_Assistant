package id.slava.nt.chatgpthelper.domain.usecase

import id.slava.nt.chatgpthelper.domain.model.UserRequest
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository

class GetGeminiResponseUseCase(private val repository: RequestRepository)  {

    suspend operator fun invoke(useSystemMessage: Boolean, geminiModel: String, userMessages: List<UserRequest>) =
        repository.getGeminiResponse(useSystemMessage, geminiModel, userMessages)

}