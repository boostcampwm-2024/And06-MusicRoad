package com.squirtles.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.datasource.local.LocalDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.AppleMusicDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.api.AppleMusicApi
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl
import com.squirtles.data.repository.AppleMusicRepositoryImpl
import com.squirtles.data.repository.FirebaseRepositoryImpl
import com.squirtles.data.repository.LocalRepositoryImpl
import com.squirtles.domain.datasource.AppleMusicRemoteDataSource
import com.squirtles.domain.datasource.FirebaseRemoteDataSource
import com.squirtles.domain.datasource.LocalDataSource
import com.squirtles.domain.repository.AppleMusicRepository
import com.squirtles.domain.repository.FirebaseRepository
import com.squirtles.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideLocalRepository(localDataSource: LocalDataSource): LocalRepository =
        LocalRepositoryImpl(localDataSource)

    @Provides
    @Singleton
    fun provideLocalDataSource(): LocalDataSource =
        LocalDataSourceImpl()

    @Provides
    @Singleton
    fun provideFirebaseRepository(firebaseRemoteDataSource: FirebaseRemoteDataSource): FirebaseRepository =
        FirebaseRepositoryImpl(firebaseRemoteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseRemoteDataSource(db: FirebaseFirestore): FirebaseRemoteDataSource =
        FirebaseDataSourceImpl(db)

    @Provides
    @Singleton
    fun provideAppleMusicRepository(appleMusicDataSource: AppleMusicRemoteDataSource): AppleMusicRepository =
        AppleMusicRepositoryImpl(appleMusicDataSource)

    @Provides
    @Singleton
    fun provideAppleMusicDataSource(api: AppleMusicApi): AppleMusicRemoteDataSource =
        AppleMusicDataSourceImpl(api)
}
