package com.example.testing.apolloexperiments.apollographql

import com.apollographql.apollo.ApolloClient

interface ApolloService{
    val client: ApolloClient
    fun setAuthToken(token: String)
}

class ApolloServiceImpl(override val client: ApolloClient,
                        val interceptor: ApolloOkHttpInterceptor): ApolloService{

    override fun setAuthToken(token: String) {
        interceptor.authorizationToken = token
    }
}