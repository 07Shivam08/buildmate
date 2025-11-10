package com.skillMatcher.buildMate.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skillMatcher.buildMate.data.converters.Converters

@Entity(tableName = "user_skills")
@TypeConverters(Converters::class)
data class UserSkillEntity(
    @PrimaryKey(autoGenerate = true)
    val skillId: Int = 0,
    val userId: String,
    val techStack: String,
    val libraries: List<String>,
    val experienceLevel: String,
    val goal: String,
    val additionalNotes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

