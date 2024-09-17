package id.slava.nt.chatgpthelper.di

import id.slava.nt.chatgpthelper.domain.usecase.GetChatGPTResponseUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        GetChatGPTResponseUseCase(repository = get())
    }

}