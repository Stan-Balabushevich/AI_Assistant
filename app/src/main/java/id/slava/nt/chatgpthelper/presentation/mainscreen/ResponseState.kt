package id.slava.nt.chatgpthelper.presentation.mainscreen

data class ResponseState(
    val isLoading: Boolean = false,
    val response: String? = null,
    val error: String? = null
)
