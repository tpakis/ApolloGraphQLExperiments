package com.example.testing.apolloexperiments.apollographql

import okhttp3.Interceptor
import okhttp3.Response

class ApolloOkHttpInterceptor: Interceptor {
    var authorizationToken = ""

    override fun intercept(chain: Interceptor.Chain?): Response {
        return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer " + authorizationToken).build())
    }
}