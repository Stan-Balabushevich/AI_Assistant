package id.slava.nt.chatgpthelper.di

import id.slava.nt.chatgpthelper.data.remote.BASE_URL
import id.slava.nt.chatgpthelper.data.remote.OpenAIApiService
import id.slava.nt.chatgpthelper.data.repository.RequestRepositoryImpl
import id.slava.nt.chatgpthelper.domain.repository.RequestRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Standard Gson converter
            .build()
            .create(OpenAIApiService::class.java)
    }

    single<RequestRepository> {
        RequestRepositoryImpl(openAIApiService = get())
    }


}