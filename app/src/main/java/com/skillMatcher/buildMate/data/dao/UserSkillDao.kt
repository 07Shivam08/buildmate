package com.skillMatcher.buildMate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillMatcher.buildMate.data.entities.UserSkillEntity


@Dao
interface UserSkillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSkill(skill: UserSkillEntity): Long  // Returns the ID of the saved skill

    @Query("SELECT * FROM user_skills ORDER BY skillId DESC LIMIT 1")
    suspend fun getLatestSkill(): UserSkillEntity?

    @Query("DELETE FROM user_skills")
    suspend fun clearAll()
}
