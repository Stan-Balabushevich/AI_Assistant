package id.slava.nt.chatgpthelper.domain.usecase

import id.slava.nt.chatgpthelper.data.remote.dt_object.Message
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository

class GetChatGPTResponseUseCase(private val repository: RequestRepository)  {

    suspend operator fun invoke(userMessages: List<Message>) = repository.getChatGPTResponse(userMessages)

}