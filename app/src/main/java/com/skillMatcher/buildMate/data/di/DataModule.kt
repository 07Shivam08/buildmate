package com.skillMatcher.buildMate.data.di


import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.skillMatcher.buildMate.data.dao.IdeaDao
import com.skillMatcher.buildMate.data.dao.UserSkillDao
import com.skillMatcher.buildMate.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "buildMate_db"
        ).build()

    @Provides
    @Singleton
    fun provideUserSkillDao(database: AppDatabase): UserSkillDao {
        return database.userSkillDao()
    }

    @Provides
    @Singleton
    fun provideIdeaDao(database: AppDatabase): IdeaDao {
        return database.ideaDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}

