package com.skillMatcher.buildMate.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ideas",
    foreignKeys = [
        ForeignKey(
            entity = UserSkillEntity::class,          // parent table
            parentColumns = ["skillId"],              // parent column (in UserSkillEntity)
            childColumns = ["skillId"],               // child column (in IdeaEntity)
            onDelete = ForeignKey.CASCADE             // delete ideas if related skill is deleted
        )
    ],
    indices = [Index("skillId"), Index("userId")] // improves query performance for lookups
)
data class IdeaEntity(
    @PrimaryKey(autoGenerate = true)
    val ideaId: Int = 0,
    val userId: String,
    //This links Idea → Skill
    val skillId: Int, // foreign key reference to UserSkillEntity.skillId
    val ideaTitle: String,
    val description: String,
    val difficulty: String,
    val techUsed: String,
    val learningFocus: String,
    val createdAt: Long = System.currentTimeMillis()
)
