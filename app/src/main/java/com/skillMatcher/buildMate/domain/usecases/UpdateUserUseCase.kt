package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.domain.repo.Repo
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repo: Repo
) {

    suspend fun updateUserUseCase(userData: UserDataModels)=repo.updateUserDetails(userData)

}