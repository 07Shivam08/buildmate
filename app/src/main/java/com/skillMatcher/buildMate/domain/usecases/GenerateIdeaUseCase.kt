package com.skillMatcher.buildMate.domain.usecases

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import com.skillMatcher.buildMate.utils.ResultState
import javax.inject.Inject

class GenerateIdeaUseCase @Inject constructor(
    private val repo: IdeaRepository
) {
    suspend operator fun invoke(skill: UserSkillEntity): ResultState<List<IdeaEntity>> {
        return repo.generateIdeasFromSkill(skill)
    }
}