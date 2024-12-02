package com.squirtles.mediaservice

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

const val SEEK_TO_DURATION = 5_000L

internal class MediaSessionCallback : MediaSession.Callback {

    @OptIn(UnstableApi::class)
    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult =

        // 컨트롤러가 미디어 알림과 연관된 컨트롤러인지 확인
        if (session.isMediaNotificationController(controller)) {
            val sessionCommandBuilder =
                MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()

            NotificationCommand.entries.forEach { commandButton ->
                commandButton.sessionCommand.apply {
                    sessionCommandBuilder.add(this)
                }
            }

            MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommandBuilder.build())
                .setCustomLayout(
                    NotificationCommand.entries
                        .filter {
                            it.customAction == NotificationCommand.SEEK_REWIND.customAction
                                    || it.customAction == NotificationCommand.SEEK_FORWARD.customAction
                        }.map {
                            CommandButton.Builder()
                                .setDisplayName(it.displayName)
                                .setIconResId(it.iconResId(session.player.isPlaying))
                                .setSessionCommand(it.sessionCommand)
                                .build()
                        }
                )
                .build()
        } else {
            MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
        }

    // 사용자 정의 명령이 수신되었을 때 호출되는 콜백
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {

        when (customCommand.customAction) {
            NotificationCommand.SEEK_REWIND.customAction -> {
                session.player.run {
                    seekTo(currentPosition - SEEK_TO_DURATION)
                }
            }

            NotificationCommand.PLAY_AND_PAUSE.customAction -> {
                if (!session.player.isPlaying) session.player.play()
                else session.player.pause()
            }

            NotificationCommand.SEEK_FORWARD.customAction -> {
                session.player.run {
                    seekTo(currentPosition + SEEK_TO_DURATION)
                }
            }

            else -> {
                // Do nothing
            }
        }

        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }
}
