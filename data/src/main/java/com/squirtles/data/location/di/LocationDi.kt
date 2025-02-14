package com.squirtles.data.location.di

import com.squirtles.data.location.LocalLocationRepositoryImpl
import com.squirtles.domain.location.LocalLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationDi{
    @Provides
    @Singleton
    fun provideLocalLocationRepository(): LocalLocationRepository =
        LocalLocationRepositoryImpl()
}
