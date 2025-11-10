package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import javax.inject.Inject

class SaveIdeaUseCase @Inject constructor(
    private val ideaRepository: IdeaRepository
) {
    suspend fun saveIdeaUseCase(idea: IdeaEntity): Long {
        return ideaRepository.saveIdea(idea)
    }
}
