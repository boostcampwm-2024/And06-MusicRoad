package com.example.applemusic.di

import com.example.applemusic.AppleMusicDataSourceImpl
import com.example.applemusic.AppleMusicRemoteDataSource
import com.example.applemusic.AppleMusicRepository
import com.example.applemusic.AppleMusicRepositoryImpl
import com.example.applemusic.api.AppleMusicApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppleMusicModule{

    private const val BASE_APPLE_MUSIC_URL = "https://api.music.apple.com/"

    @Provides
    @Singleton
    fun provideAppleMusicApi(
        appleOkHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): AppleMusicApi {
        return Retrofit.Builder()
            .baseUrl(BASE_APPLE_MUSIC_URL)
            .addConverterFactory(converterFactory)
            .client(appleOkHttpClient)
            .build()
            .create(AppleMusicApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppleMusicRepository(appleMusicDataSource: AppleMusicRemoteDataSource): AppleMusicRepository =
        AppleMusicRepositoryImpl(appleMusicDataSource)

    @Provides
    @Singleton
    fun provideAppleMusicDataSource(api: AppleMusicApi): AppleMusicRemoteDataSource =
        AppleMusicDataSourceImpl(api)
}
