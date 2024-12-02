package com.squirtles.mediaservice.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.squirtles.mediaservice.ConnectedMediaController
import com.squirtles.mediaservice.MediaControllerManager
import com.squirtles.mediaservice.MediaNotificationManager
import com.squirtles.mediaservice.MediaPlayerService
import com.squirtles.mediaservice.MediaSessionCallback
import com.squirtles.mediaservice.Notifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class MediaServiceBinds {

    @Binds
    abstract fun bindsNotifier(
        mediaNotification: MediaNotificationManager
    ): Notifier

    @Binds
    abstract fun bindsMediaControllerManager(
        connectedMediaController: ConnectedMediaController
    ): MediaControllerManager
}

@Module
@InstallIn(SingletonComponent::class)
object MediaServiceModule {

    @Singleton
    @Provides
    fun providesExoPlayer(
        @ApplicationContext context: Context,
    ): ExoPlayer =
        ExoPlayer.Builder(context)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()

    @Singleton
    @Provides
    fun providesMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer,
    ): MediaSession =
        MediaSession.Builder(context, player)
            .setCallback(MediaSessionCallback())
            .build()

    @Singleton
    @Provides
    fun providesMediaNotificationManager(
        @ApplicationContext context: Context,
        mediaSession: MediaSession,
    ): MediaNotificationManager =
        MediaNotificationManager(context, mediaSession)

    @Singleton
    @Provides
    fun providesSessionToken(
        @ApplicationContext context: Context
    ): SessionToken =
        SessionToken(context, ComponentName(context, MediaPlayerService::class.java))

    @Singleton
    @Provides
    fun providesListenableFutureMediaController(
        @ApplicationContext context: Context,
        sessionToken: SessionToken
    ): ListenableFuture<MediaController> =
        MediaController
            .Builder(context, sessionToken)
            .buildAsync()
}
