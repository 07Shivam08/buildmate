package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class FetchUserDataUseCase @Inject constructor(
    private val repo: Repo
){
    suspend fun fetchUserDataUseCase() = repo.fetchUserData()

}