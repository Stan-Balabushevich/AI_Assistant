package id.slava.nt.chatgpthelper.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.slava.nt.chatgpthelper.common.GPTModels
import id.slava.nt.chatgpthelper.common.GeminiModels
import id.slava.nt.chatgpthelper.common.Resource
import id.slava.nt.chatgpthelper.domain.model.UserRequest
import id.slava.nt.chatgpthelper.domain.usecase.GetChatGPTResponseUseCase
import id.slava.nt.chatgpthelper.domain.usecase.GetGeminiResponseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val getChatGPTResponseUseCase: GetChatGPTResponseUseCase,
    private val getGeminiResponseUseCase: GetGeminiResponseUseCase
) : ViewModel() {

    private val _responseState = MutableStateFlow(ResponseState())
    val responseState: StateFlow<ResponseState> = _responseState

    private var gptModel = GPTModels.GPT_4_TURBO
    private var geminiModel = GeminiModels.GEMINI_1_5_FLASH


    fun getChatGPTResponse(userMessages: List<UserRequest>) {
        viewModelScope.launch {
            getChatGPTResponseUseCase(gptModel.modelName, userMessages).collect { result ->
                when (result) {
                    is Resource.Error -> _responseState.value =
                        ResponseState(error = result.message ?: "An unexpected error occurred")

                    is Resource.Loading -> _responseState.value = ResponseState(isLoading = true)
                    is Resource.Success -> _responseState.value =
                        ResponseState(response = result.data?.response)
                }
            }
        }
    }

    fun getGeminiResponse(userMessages: List<UserRequest>) {
        viewModelScope.launch {
            getGeminiResponseUseCase(geminiModel.modelName, userMessages).collect { result ->
                when (result) {
                    is Resource.Error -> _responseState.value =
                        ResponseState(error = result.message ?: "An unexpected error occurred")

                    is Resource.Loading -> _responseState.value = ResponseState(isLoading = true)
                    is Resource.Success -> _responseState.value =
                        ResponseState(response = result.data?.response)
                }
            }
        }
    }

    fun updateGptModel(model: GPTModels) {
        gptModel = model
    }

    fun updateGeminiModel(model: GeminiModels) {
        geminiModel = model
    }

}