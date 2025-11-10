package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import javax.inject.Inject

class FetchAllIdeasUseCase @Inject constructor(
    private val ideaRepository: IdeaRepository
) {
    suspend fun fetchAllIdeasUseCase(userId: String): List<IdeaEntity> {
        return ideaRepository.getAllIdeas(userId)
    }
}
