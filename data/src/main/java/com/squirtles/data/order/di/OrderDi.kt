package com.squirtles.data.order.di

import com.squirtles.data.order.LocalPickListOrderRepositoryImpl
import com.squirtles.domain.order.LocalPickListOrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderDi {
    @Provides
    @Singleton
    fun provideLocalPickListOrderRepository(): LocalPickListOrderRepository =
        LocalPickListOrderRepositoryImpl()
}
