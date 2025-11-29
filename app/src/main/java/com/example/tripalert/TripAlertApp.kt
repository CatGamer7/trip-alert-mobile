package com.example.tripalert

import android.app.Application
import com.example.tripalert.di.appModule
import com.example.tripalert.di.networkModule
import com.example.tripalert.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TripAlertApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TripAlertApp)
            modules(listOf(appModule, userModule, networkModule))
        }
    }
}
