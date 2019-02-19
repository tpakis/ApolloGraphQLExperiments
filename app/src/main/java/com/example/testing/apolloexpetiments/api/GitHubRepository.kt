package com.example.testing.apolloexpetiments.api

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.*
import com.example.testing.apolloexperiments.apollographql.ApolloService
import com.example.testing.apolloexpetiments.BuildConfig
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.experimental.suspendCoroutine

typealias GitHubResponse = SearchRepositoriesQuery.Data

class GitHubRepository @Inject constructor(val service: ApolloService){


    suspend fun searchRepositories(term: String): Result<GitRepoDomainModel> = suspendCoroutine { continuation ->
        //change the auth token on the fly, we put it in build config but you can fetch it from
        //any different place like shared preferences or local variable
        service.setAuthToken(BuildConfig.AUTH_TOKEN)
        service.client.query(SearchRepositoriesQuery.builder().searchQuery(term).build())
                .enqueue(object : ApolloCall.Callback<GitHubResponse?>() {
                    override fun onCanceledError(e: ApolloCanceledException) {
                        super.onCanceledError(e)
                        Timber.d(e.localizedMessage)
                    }

                    override fun onNetworkError(e: ApolloNetworkException) {
                        super.onNetworkError(e)
                        Timber.d(e.localizedMessage)
                    }

                    override fun onParseError(e: ApolloParseException) {
                        super.onParseError(e)
                        Timber.d(e.localizedMessage)
                    }

                    override fun onHttpError(e: ApolloHttpException) {
                        super.onHttpError(e)
                        Timber.d(e.localizedMessage)
                    }

                    override fun onFailure(e: ApolloException) {
                        continuation.resume(Failure(e))
                    }

                    override fun onResponse(response: Response<GitHubResponse?>) {
                        if (response.data() == null) {
                            continuation.resume(Failure(Throwable("Response is null")))
                            return
                        } else {
                            continuation.resume(Success(response.data()!!.toGitHubDomainModel()))
                        }
                    }
                })
    }

}