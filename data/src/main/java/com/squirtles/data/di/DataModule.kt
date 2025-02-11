package com.squirtles.data.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.datasource.local.LocalDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.AppleMusicDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.api.AppleMusicApi
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl
import com.squirtles.data.repository.AppleMusicRepositoryImpl
import com.squirtles.data.repository.FirebaseRepositoryImpl
import com.squirtles.data.repository.LocalRepositoryImpl
import com.squirtles.domain.applemusic.AppleMusicRemoteDataSource
import com.squirtles.domain.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.local.LocalDataSource
import com.squirtles.domain.applemusic.AppleMusicRepository
import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.local.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataSource =
        LocalDataSourceImpl(context)

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
