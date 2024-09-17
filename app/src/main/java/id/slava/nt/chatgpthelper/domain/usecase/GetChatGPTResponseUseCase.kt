package id.slava.nt.chatgpthelper.domain.usecase

import id.slava.nt.chatgpthelper.domain.repository.RequestRepository

class GetChatGPTResponseUseCase(private val repository: RequestRepository)  {

    suspend operator fun invoke(userMessage: String) = repository.getChatGPTResponse(userMessage)

}