package com.squirtles.data.favorite.di

import com.google.firebase.firestore.FirebaseFirestore
import com.squirtles.data.favorite.FirebaseFavoriteDataSourceImpl
import com.squirtles.data.favorite.FirebaseFavoriteRepositoryImpl
import com.squirtles.data.favorite.CloudFunctionHelper
import com.squirtles.domain.favorite.FirebaseFavoriteDataSource
import com.squirtles.domain.favorite.FirebaseFavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseFavoriteDi {

    @Provides
    @Singleton
    fun provideFirebaseFavoriteRepository(firebaseFavoriteDataSource: FirebaseFavoriteDataSource): FirebaseFavoriteRepository =
        FirebaseFavoriteRepositoryImpl(firebaseFavoriteDataSource)

    @Provides
    @Singleton
    fun provideFirebaseFavoriteDataSource(db: FirebaseFirestore, cloudFunctionHelper: CloudFunctionHelper): FirebaseFavoriteDataSource =
        FirebaseFavoriteDataSourceImpl(db, cloudFunctionHelper)

    @Provides
    @Singleton
    fun provideCloudFunctionHelper(): CloudFunctionHelper = CloudFunctionHelper()
}
