package com.example.user.di

import android.content.Context
import com.example.user.FirebaseUserDataSource
import com.example.user.FirebaseUserDataSourceImpl
import com.example.user.FirebaseUserRepository
import com.example.user.FirebaseUserRepositoryImpl
import com.example.user.LocalUserDataSource
import com.example.user.LocalUserDataSourceImpl
import com.example.user.LocalUserRepository
import com.example.user.LocalUserRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDi {
    @Provides
    @Singleton
    fun provideLocalUserRepository(localUserDataSource: LocalUserDataSource): LocalUserRepository =
        LocalUserRepositoryImpl(localUserDataSource)

    @Provides
    @Singleton
    fun provideLocalUserDataSource(@ApplicationContext context: Context): LocalUserDataSource =
        LocalUserDataSourceImpl(context)

    @Provides
    @Singleton
    fun provideFirebaseUserRepository(firebaseUserDataSource: FirebaseUserDataSource): FirebaseUserRepository =
        FirebaseUserRepositoryImpl(firebaseUserDataSource)

    @Provides
    @Singleton
    fun provideFirebaseUserDataSource(db: FirebaseFirestore): FirebaseUserDataSource =
        FirebaseUserDataSourceImpl(db)
}
