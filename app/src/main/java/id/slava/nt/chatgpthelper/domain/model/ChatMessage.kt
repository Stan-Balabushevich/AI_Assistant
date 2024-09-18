package id.slava.nt.chatgpthelper.domain.model

//data class ChatMessage(
//    val content: String,
//    val isUser: Boolean,
//    val isLoading: Boolean = false,
//)


sealed class ChatMessage {
    data class UserMessage(val content: String) : ChatMessage()
    data class BotMessage(val content: String) : ChatMessage()
    data object LoadingMessage : ChatMessage()
    data class ErrorMessage(val content: String) : ChatMessage()
}


