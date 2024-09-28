package id.slava.nt.chatgpthelper.di

import id.slava.nt.chatgpthelper.presentation.mainscreen.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

//    viewModel {
//        MainScreenViewModel(
//            getChatGPTResponseUseCase = get(),
//            getGeminiResponseUseCase = get(),
//            getTruncatedHistoryUseCase = get()
//        )
//    }

    // same as above, but no need to add use cases to constructor
    viewModelOf(::MainScreenViewModel)

}