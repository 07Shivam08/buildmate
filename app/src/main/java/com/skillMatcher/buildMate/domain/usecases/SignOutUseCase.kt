package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repo: Repo
){
    suspend fun signOutUseCase() = repo.signOut()
}