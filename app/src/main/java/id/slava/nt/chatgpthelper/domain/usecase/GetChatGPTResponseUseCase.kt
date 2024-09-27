package id.slava.nt.chatgpthelper.domain.usecase

import id.slava.nt.chatgpthelper.domain.model.UserRequest
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository

class GetChatGPTResponseUseCase(private val repository: RequestRepository)  {

    suspend operator fun invoke(gptModel: String, userMessages: List<UserRequest>) = repository.getChatGPTResponse(gptModel, userMessages)

}