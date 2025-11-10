package com.skillMatcher.buildMate.domain.usecases


import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class FetchUserNameUseCase @Inject constructor(
    private val repo: Repo
) {
    suspend fun fetchUserNameUseCase() = repo.fetchUserName()
}