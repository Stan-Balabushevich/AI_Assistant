package id.slava.nt.chatgpthelper

import android.app.Application
import id.slava.nt.chatgpthelper.di.dataModule
import id.slava.nt.chatgpthelper.di.domainModule
import id.slava.nt.chatgpthelper.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChatGPThelperApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ChatGPThelperApp)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}