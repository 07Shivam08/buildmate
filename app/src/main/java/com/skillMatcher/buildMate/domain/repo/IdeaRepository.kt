package com.skillMatcher.buildMate.domain.repo

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.utils.ResultState

interface IdeaRepository {
    suspend fun generateIdeasFromSkill(skill: UserSkillEntity): ResultState<List<IdeaEntity>>
    suspend fun saveIdea(idea: IdeaEntity): Long  // Returns the ID of the saved idea
    suspend fun getAllIdeas(userId: String): List<IdeaEntity>
    suspend fun getIdeasByIdeaId(userId: String, ideaId: Int): List<IdeaEntity>
}


