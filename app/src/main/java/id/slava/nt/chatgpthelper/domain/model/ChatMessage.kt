package id.slava.nt.chatgpthelper.domain.model

sealed class ChatMessage {
    data class UserMessage(val content: String) : ChatMessage()
    data class BotMessage(val content: String) : ChatMessage()
    data object LoadingMessage : ChatMessage()
    data class ErrorMessage(val content: String) : ChatMessage()
}


