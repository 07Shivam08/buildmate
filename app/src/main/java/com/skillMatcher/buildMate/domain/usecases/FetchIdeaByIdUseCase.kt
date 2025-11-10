package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import javax.inject.Inject

class FetchIdeaByIdUseCase @Inject constructor(
    private val ideaRepository: IdeaRepository
) {
    suspend fun fetchIdeaByIdUseCase(userId: String, ideaId: Int): List<IdeaEntity> {
        return ideaRepository.getIdeasByIdeaId(userId, ideaId)
    }
}
