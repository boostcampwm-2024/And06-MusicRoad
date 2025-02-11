package com.squirtles.data.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.datasource.local.LocalDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.AppleMusicDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.api.AppleMusicApi
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl
import com.squirtles.data.repository.local.LocalRepositoryImpl
import com.squirtles.data.repository.remote.applemusic.AppleMusicRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebaseFavoriteRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebasePickRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebaseRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebaseUserRepositoryImpl
import com.squirtles.domain.datasource.local.LocalDataSource
import com.squirtles.domain.datasource.remote.applemusic.AppleMusicRemoteDataSource
import com.squirtles.domain.datasource.remote.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.repository.remote.applemusic.AppleMusicRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseFavoriteRepository
import com.squirtles.domain.repository.remote.firebase.FirebasePickRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
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
    fun provideFirebaseFavoriteRepository(firebaseRemoteDataSource: FirebaseRemoteDataSource): FirebaseFavoriteRepository =
        FirebaseFavoriteRepositoryImpl(firebaseRemoteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseUserRepository(firebaseRemoteDataSource: FirebaseRemoteDataSource): FirebaseUserRepository =
        FirebaseUserRepositoryImpl(firebaseRemoteDataSource)

    @Provides
    @Singleton
    fun provideFirebasePickRepository(firebaseRemoteDataSource: FirebaseRemoteDataSource): FirebasePickRepository =
        FirebasePickRepositoryImpl(firebaseRemoteDataSource)


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
