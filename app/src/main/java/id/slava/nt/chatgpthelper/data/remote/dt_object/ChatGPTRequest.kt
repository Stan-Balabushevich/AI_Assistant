package id.slava.nt.chatgpthelper.data.remote.dt_object

import id.slava.nt.chatgpthelper.domain.model.GptResponse

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

fun ChatGPTResponse.toGptResponse(): GptResponse {
    val response = choices.firstOrNull()?.message?.content
    return GptResponse(response)

}
