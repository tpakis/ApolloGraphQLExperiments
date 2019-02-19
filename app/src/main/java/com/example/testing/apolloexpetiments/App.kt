package com.example.testing.apolloexpetiments

import android.app.Application
import com.example.testing.apolloexpetiments.di.AppComponent
import com.example.testing.apolloexpetiments.di.AppModule
import com.example.testing.apolloexpetiments.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import timber.log.Timber

class App: Application(){
    companion object {

        private lateinit var appContext: App
        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent = appComponent


        fun getAppInstance():App = appContext
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appContext = this
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(appContext))
                .build()
        setupStetho()
    }

    private fun setupStetho() {
        if (BuildConfig.DEBUG) {
            Runnable { Stetho.initializeWithDefaults(appContext) }.run()
        }
    }
}