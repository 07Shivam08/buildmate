package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val repo: Repo)  {

    suspend fun createUserUseCase(userData: UserDataModels) = repo.registerUserWithEmailAndPassword(userData)
}