package com.skillMatcher.buildMate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillMatcher.buildMate.data.entities.IdeaEntity
import retrofit2.http.GET

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIdea(idea: IdeaEntity): Long  // Returns the ID of the inserted row

    @Query("SELECT * FROM ideas WHERE userId = :userId AND ideaId = :ideaId ORDER BY createdAt DESC")
    suspend fun getIdeasByIdeaId(userId: String, ideaId: Int): List<IdeaEntity>

    @Query ("SELECT * FROM ideas WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getAllIdeas(userId: String): List<IdeaEntity>


    @Query("DELETE FROM ideas WHERE skillId = :skillId")
    suspend fun deleteIdeasBySkill(skillId: Int)
}

