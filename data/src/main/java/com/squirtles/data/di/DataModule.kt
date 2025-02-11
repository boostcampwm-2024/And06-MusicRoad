package com.squirtles.data.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.datasource.local.LocalDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.AppleMusicDataSourceImpl
import com.squirtles.data.datasource.remote.applemusic.api.AppleMusicApi
import com.squirtles.data.datasource.remote.firebase.CloudFunctionHelper
import com.squirtles.data.datasource.remote.firebase.FirebaseFavoriteDataSourceImpl
import com.squirtles.data.datasource.remote.firebase.FirebasePickDataSourceImpl
import com.squirtles.data.datasource.remote.firebase.FirebaseUserDataSourceImpl
import com.squirtles.data.repository.local.LocalRepositoryImpl
import com.squirtles.data.repository.remote.applemusic.AppleMusicRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebaseFavoriteRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebasePickRepositoryImpl
import com.squirtles.data.repository.remote.firebase.FirebaseUserRepositoryImpl
import com.squirtles.domain.datasource.local.LocalDataSource
import com.squirtles.domain.datasource.remote.applemusic.AppleMusicRemoteDataSource
import com.squirtles.domain.datasource.remote.firebase.FirebaseFavoriteDataSource
import com.squirtles.domain.datasource.remote.firebase.FirebasePickDataSource
import com.squirtles.domain.datasource.remote.firebase.FirebaseUserDataSource
import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.repository.remote.applemusic.AppleMusicRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseFavoriteRepository
import com.squirtles.domain.repository.remote.firebase.FirebasePickRepository
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

    // LOCAL

    @Provides
    @Singleton
    fun provideLocalRepository(localDataSource: LocalDataSource): LocalRepository =
        LocalRepositoryImpl(localDataSource)

    @Provides
    @Singleton
    fun provideLocalDataSource(@ApplicationContext context: Context): LocalDataSource =
        LocalDataSourceImpl(context)

    // FIREBASE REPOSITORY

    @Provides
    @Singleton
    fun provideFirebaseFavoriteRepository(firebaseFavoriteDataSource: FirebaseFavoriteDataSource): FirebaseFavoriteRepository =
        FirebaseFavoriteRepositoryImpl(firebaseFavoriteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseUserRepository(firebaseUserDataSource: FirebaseUserDataSource): FirebaseUserRepository =
        FirebaseUserRepositoryImpl(firebaseUserDataSource)

    @Provides
    @Singleton
    fun provideFirebasePickRepository(firebasePickDataSource: FirebasePickDataSource): FirebasePickRepository =
        FirebasePickRepositoryImpl(firebasePickDataSource)


    // FIREBASE DATA SOURCE

    @Provides
    @Singleton
    fun provideFirebasePickDataSource(db: FirebaseFirestore): FirebasePickDataSource =
        FirebasePickDataSourceImpl(db)

    @Provides
    @Singleton
    fun provideFirebaseFavoriteDataSource(db: FirebaseFirestore, cloudFunctionHelper: CloudFunctionHelper): FirebaseFavoriteDataSource =
        FirebaseFavoriteDataSourceImpl(db, cloudFunctionHelper)

    @Provides
    @Singleton
    fun provideFirebaseUserDataSource(db: FirebaseFirestore): FirebaseUserDataSource =
        FirebaseUserDataSourceImpl(db)

    @Provides
    @Singleton
    fun provideCloudFunctionHelper(): CloudFunctionHelper = CloudFunctionHelper()

    // APPLE MUSIC

    @Provides
    @Singleton
    fun provideAppleMusicRepository(appleMusicDataSource: AppleMusicRemoteDataSource): AppleMusicRepository =
        AppleMusicRepositoryImpl(appleMusicDataSource)

    @Provides
    @Singleton
    fun provideAppleMusicDataSource(api: AppleMusicApi): AppleMusicRemoteDataSource =
        AppleMusicDataSourceImpl(api)
}
