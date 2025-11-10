package com.skillMatcher.buildMate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skillMatcher.buildMate.data.converters.Converters
import com.skillMatcher.buildMate.data.dao.IdeaDao
import com.skillMatcher.buildMate.data.dao.UserSkillDao
import com.skillMatcher.buildMate.data.entities.IdeaEntity
import com.skillMatcher.buildMate.data.entities.UserSkillEntity

@Database(entities = [UserSkillEntity::class, IdeaEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userSkillDao(): UserSkillDao
    abstract fun ideaDao(): IdeaDao
}
