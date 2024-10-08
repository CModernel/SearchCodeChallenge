package com.sonder.di

import com.sonder.data.MockRequestsRepositoryImpl
import com.sonder.data.MockResponsesRepositoryImpl
import com.sonder.data.SearchRepositoryImpl
import com.sonder.data.SearchStateRepositoryImpl
import com.sonder.domain.repositories.MockRequestsRepository
import com.sonder.domain.repositories.MockResponsesRepository
import com.sonder.domain.repositories.SearchRepository
import com.sonder.domain.repositories.SearchStateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideSearchRepository(): SearchRepository {
        return SearchRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSearchResultRepository(): SearchStateRepository {
        return SearchStateRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideRequests(): MockRequestsRepository {
        return MockRequestsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideResponse(): MockResponsesRepository {
        return MockResponsesRepositoryImpl()
    }
}