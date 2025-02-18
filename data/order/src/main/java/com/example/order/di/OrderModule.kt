package com.example.order.di

import com.example.order.LocalPickListOrderRepository
import com.example.order.LocalPickListOrderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {
    @Provides
    @Singleton
    fun provideLocalPickListOrderRepository(): LocalPickListOrderRepository =
        LocalPickListOrderRepositoryImpl()
}
