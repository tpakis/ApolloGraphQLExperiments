package com.example.testing.apolloexperiments.apollographql

import android.provider.Settings
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import type.CustomType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Singleton

@Module
class ApolloServiceModule {

    @Provides
    @Singleton
    fun provideApolloOkHttpInterceptor(): ApolloOkHttpInterceptor {
        return ApolloOkHttpInterceptor()
    }


    @Provides
    @Singleton
    fun provideApolloService(interceptor: ApolloOkHttpInterceptor): ApolloService{
        val jsonCustomTypeAdapter = object : CustomTypeAdapter<String> {
            override fun decode(value: CustomTypeValue<*>): String {
                return value.value as String
            }

            override fun encode(value: String): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(value)
            }
        }

        val dateTimeCustomTypeAdapter = object : CustomTypeAdapter<Date> {
            private val ISO8601 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

            override fun decode(value: CustomTypeValue<*>): Date {
                try {
                    return ISO8601.parse(value.value.toString().replace("Z", "+00:00") )
                } catch (e: ParseException) {
                    throw IllegalArgumentException(value.toString() + " is not a valid ISO 8601 date", e)
                }

            }

            override fun encode(value: Date): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(ISO8601.format(value));
            }

        }

        val dateCustomTypeAdapter = object : CustomTypeAdapter<Date> {
            override fun decode(value: CustomTypeValue<*>): Date {
                try {
                    val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val result = df.parse(value.value.toString())
                    return result
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }

            }

            override fun encode(value: Date): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(Settings.System.DATE_FORMAT.format(value))
            }
        }

        val stetho = StethoInterceptor()
        val okHttp = OkHttpClient
                .Builder()
                .addNetworkInterceptor(stetho)
                .addInterceptor(interceptor)
                .build()
        val client =  ApolloClient.builder()
                .serverUrl("https://api.github.com/graphql")
                .addCustomTypeAdapter(CustomType.JSON, jsonCustomTypeAdapter)
                .addCustomTypeAdapter(CustomType.DATETIME, dateTimeCustomTypeAdapter)
                .okHttpClient(okHttp)
                .build()
        return ApolloServiceImpl(client, interceptor)
    }

}