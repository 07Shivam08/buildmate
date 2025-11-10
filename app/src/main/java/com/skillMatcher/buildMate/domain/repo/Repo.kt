package com.skillMatcher.buildMate.domain.repo

import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity
import com.skillMatcher.buildMate.domain.models.UserDataModels
import com.skillMatcher.buildMate.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailAndPassword(userData : UserDataModels) : Flow<ResultState<String>>

    fun loginUserWithEmailAndPassword(userData : UserDataModels) : Flow<ResultState<String>>

    fun fetchUserName(): Flow<ResultState<String>>

    fun fetchUserData(): Flow<ResultState<UserDataModels>>

    fun signOut() : Flow<ResultState<String>>

    fun updateUserDetails(userData: UserDataModels): Flow<ResultState<String>>

    fun deleteAccount(): Flow<ResultState<String>>

    //fun generateIdeasFromSkill(skill: UserSkillEntity): ResultState<List<IdeaEntity>>
}