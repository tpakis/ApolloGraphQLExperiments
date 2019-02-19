package com.example.testing.apolloexpetiments.di


import com.example.testing.apolloexpetiments.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
        @get:Provides
        @get:Singleton
        val app: App
) {
//

}