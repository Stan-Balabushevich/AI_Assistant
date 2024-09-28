package id.slava.nt.chatgpthelper.di

import id.slava.nt.chatgpthelper.domain.usecase.GetChatGPTResponseUseCase
import id.slava.nt.chatgpthelper.domain.usecase.GetGeminiResponseUseCase
import id.slava.nt.chatgpthelper.domain.usecase.GetTruncatedHistoryUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetChatGPTResponseUseCase(repository = get())
    }

    factory {
        GetGeminiResponseUseCase(repository = get())
    }

    factory {
        GetTruncatedHistoryUseCase()
    }

}