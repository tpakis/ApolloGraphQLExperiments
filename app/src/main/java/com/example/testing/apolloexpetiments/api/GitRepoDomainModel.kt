package com.example.testing.apolloexpetiments.api

data class GitRepoDomainModel(
        val repositories: List<Repository>
)

data class Repository(
        val name: String,
        val forkCount: Int
)

fun GitHubResponse.toGitHubDomainModel(): GitRepoDomainModel {
    val repos = search().edges()?.map { edge ->
        val node = (edge.node() as SearchRepositoriesQuery.AsRepository)
        Repository(
                name = node.name(),
                forkCount = node.forkCount()
        )
    } ?: emptyList()
    return GitRepoDomainModel(
            repositories = repos
    )
}