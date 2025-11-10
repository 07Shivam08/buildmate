package com.skillMatcher.buildMate.domain.di

import com.skillMatcher.buildMate.data.repoImplementation.RepoImpl
import com.skillMatcher.buildMate.data.repoImplementation.IdeaRepositoryImpl
import com.skillMatcher.buildMate.domain.repo.Repo
import com.skillMatcher.buildMate.domain.repo.IdeaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindRepo(repoImpl: RepoImpl): Repo

    @Binds
    @Singleton
    abstract fun bindIdeaRepository(impl: IdeaRepositoryImpl): IdeaRepository
}