package id.slava.nt.chatgpthelper.domain.model

data class UserRequest(
    val isUser: Boolean = true,
    val role: String,
    val content: String
)