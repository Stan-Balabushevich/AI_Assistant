package id.slava.nt.chatgpthelper.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.domain.usecase.GetChatGPTResponseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(private val getChatGPTResponseUseCase: GetChatGPTResponseUseCase): ViewModel() {

    private val _responseState = MutableStateFlow(ResponseState())
    val responseState: StateFlow<ResponseState> = _responseState



    fun getChatGPTResponse(userMessage: String) {
        viewModelScope.launch {
            getChatGPTResponseUseCase(userMessage).collect { result ->
                when(result) {
                    is Resource.Error -> _responseState.value = ResponseState(error = result.message ?: "An unexpected error occurred")
                    is Resource.Loading -> _responseState.value = ResponseState(isLoading = true)
                    is Resource.Success -> _responseState.value = ResponseState(response = result.data?.response)
                }
            }
        }
    }

}