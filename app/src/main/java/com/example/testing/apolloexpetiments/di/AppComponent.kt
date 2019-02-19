package com.example.testing.apolloexpetiments.di

import com.example.testing.apolloexperiments.apollographql.ApolloServiceModule
import com.example.testing.apolloexpetiments.App
import com.example.testing.apolloexpetiments.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ApolloServiceModule::class
])
interface AppComponent {
    val app: App

    fun inject(activity: MainActivity)
    fun inject(app: App)
}