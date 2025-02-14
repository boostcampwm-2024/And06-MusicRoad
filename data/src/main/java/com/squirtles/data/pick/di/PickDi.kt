package com.squirtles.data.pick.di

import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.pick.FirebasePickDataSourceImpl
import com.squirtles.data.pick.FirebasePickRepositoryImpl
import com.squirtles.domain.pick.FirebasePickDataSource
import com.squirtles.domain.pick.FirebasePickRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PickDi{

    @Provides
    @Singleton
    fun provideFirebasePickRepository(firebasePickDataSource: FirebasePickDataSource): FirebasePickRepository =
        FirebasePickRepositoryImpl(firebasePickDataSource)

    @Provides
    @Singleton
    fun provideFirebasePickDataSource(db: FirebaseFirestore): FirebasePickDataSource =
        FirebasePickDataSourceImpl(db)
}
