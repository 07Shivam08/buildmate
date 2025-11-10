package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import javax.inject.Inject

class SaveSkillUseCase @Inject constructor(
    private val ideaRepository: IdeaRepository
) {
    suspend fun saveSkillUseCase(skill: UserSkillEntity): Long {
        return ideaRepository.saveSkill(skill)
    }
}
