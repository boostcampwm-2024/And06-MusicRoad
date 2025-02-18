package com.example.pick.di

import com.example.pick.FirebasePickDataSource
import com.example.pick.FirebasePickDataSourceImpl
import com.example.pick.FirebasePickRepository
import com.example.pick.FirebasePickRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PickModule {

    @Provides
    @Singleton
    fun provideFirebasePickRepository(firebasePickDataSource: FirebasePickDataSource): FirebasePickRepository =
        FirebasePickRepositoryImpl(firebasePickDataSource)

    @Provides
    @Singleton
    fun provideFirebasePickDataSource(db: FirebaseFirestore): FirebasePickDataSource =
        FirebasePickDataSourceImpl(db)
}
