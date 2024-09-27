package id.slava.nt.chatgpthelper.data.remote.dt_object

import id.slava.nt.chatgpthelper.domain.model.BotResponse
import id.slava.nt.chatgpthelper.domain.model.UserRequest

data class ChatGPTRequest(
    val model: String = "gpt-4o-mini", // Model name, e.g., "gpt-4"
    val messages: List<Message>
)

data class Message(
    val role: String, // Typically "user" or "assistant"
    val content: String
)

data class ChatGPTResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

fun ChatGPTResponse.toGptResponse(): BotResponse {
    val response = choices.firstOrNull()?.message?.content
    return BotResponse(response)

}

fun Message.toUserRequest(): UserRequest =
    UserRequest(
        role = role,
        content = content
    )

fun UserRequest.toMessage(): Message =
    Message(
        role = role,
        content = content
    )
