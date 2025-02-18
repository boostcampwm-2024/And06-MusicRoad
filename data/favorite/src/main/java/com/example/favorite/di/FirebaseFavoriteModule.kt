package com.example.favorite.di

import com.example.favorite.CloudFunctionHelper
import com.example.favorite.FirebaseFavoriteDataSource
import com.example.favorite.FirebaseFavoriteDataSourceImpl
import com.example.favorite.FirebaseFavoriteRepository
import com.example.favorite.FirebaseFavoriteRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseFavoriteModule {

    @Provides
    @Singleton
    fun provideFirebaseFavoriteRepository(firebaseFavoriteDataSource: FirebaseFavoriteDataSource): FirebaseFavoriteRepository =
        FirebaseFavoriteRepositoryImpl(firebaseFavoriteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseFavoriteDataSource(
        db: FirebaseFirestore,
        cloudFunctionHelper: CloudFunctionHelper
    ): FirebaseFavoriteDataSource =
        FirebaseFavoriteDataSourceImpl(db, cloudFunctionHelper)

    @Provides
    @Singleton
    fun provideCloudFunctionHelper(): CloudFunctionHelper = CloudFunctionHelper()
}
