package com.skillMatcher.buildMate.domain.usecases


import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
class DeleteAccountUseCase @Inject constructor(private val repo: Repo) {
    suspend fun deleteAccountUseCase() = repo.deleteAccount()
}