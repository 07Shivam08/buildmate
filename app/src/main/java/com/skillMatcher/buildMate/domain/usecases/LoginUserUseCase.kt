package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val repo: Repo)  {

    suspend fun loginUserUseCase(userData: UserDataModels) = repo.loginUserWithEmailAndPassword(userData)

}