package com.example.order.di

import com.example.location.LocalLocationRepository
import com.example.order.LocalLocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideLocalLocationRepository(): LocalLocationRepository =
        LocalLocationRepositoryImpl()
}
