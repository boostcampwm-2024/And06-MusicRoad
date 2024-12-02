package com.squirtles.musicroad.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.PermissionChecker
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.squirtles.mediaservice.MediaPlayerService
import com.squirtles.musicroad.R
import com.squirtles.musicroad.main.navigations.MainNavGraph
import com.squirtles.musicroad.main.navigations.MainNavigationActions
import com.squirtles.musicroad.ui.theme.MusicRoadTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val intent = Intent(applicationContext, MediaPlayerService::class.java)
        applicationContext.startService(intent)

        setKeepOnScreenCondition(splashScreen)
        enableEdgeToEdge()

        if (!checkSelfPermission()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION_CODE)
        } else {
            setMusicRoadContent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val deniedPermission = permissions.filterIndexed { index, _ ->
            grantResults[index] == -1
        }

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (deniedPermission.isEmpty()) { // 모든 권한이 허용된 경우
                setMusicRoadContent()
            } else { // 권한이 하나라도 거부된 경우
                if (shouldShowRequestPermissionRationale(deniedPermission[0])) { // 권한 요청 가능 시 재요청
                    showNeedPermissionDialog(true) {
                        showNeedPermissionDialog(false)
                        requestPermissions(deniedPermission.toTypedArray(), REQUEST_PERMISSION_CODE)
                    }
                } else { // 권한 2번 거절 시
                    mainViewModel.setCanRequestPermission(false)
                    showPermissionSnackbar()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkSelfPermission()) {
            setMusicRoadContent()
        } else if (mainViewModel.canRequestPermission.not()) {
            showPermissionSnackbar()
        }
    }

    private fun setKeepOnScreenCondition(splashScreen: SplashScreen) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    withTimeout(30_000L) {
                        mainViewModel.loadingState.collect { state ->
                            when (state) {
                                is LoadingState.Loading -> {
                                    splashScreen.setKeepOnScreenCondition { true }
                                }

                                is LoadingState.Success -> {
                                    Log.d("MainActivity", "Success: ${state.userId}")
                                    splashScreen.setKeepOnScreenCondition { false }
                                    cancel()
                                }

                                is LoadingState.NetworkError -> {
                                    showToast(getString(R.string.main_network_error_message))
                                    finish()
                                }

                                is LoadingState.UserNotFoundError -> {
                                    showToast(getString(R.string.main_user_not_found_message))
                                    finish()
                                }

                                is LoadingState.CreatedUserError -> {
                                    showToast(getString(R.string.main_create_user_fail_message))
                                    finish()
                                }
                            }
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    showToast(getString(R.string.main_network_error_message))
                    finish()
                }
            }
        }
    }

    private fun checkSelfPermission(): Boolean {
        return PERMISSIONS.all { permission ->
            PermissionChecker.checkSelfPermission(this, permission) ==
                    PermissionChecker.PERMISSION_GRANTED
        }
    }

    private fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setMusicRoadContent() {
        setContent {
            MusicRoadTheme {
                val navController = rememberNavController()
                val navigationActions = remember(navController) {
                    MainNavigationActions(navController)
                }
                MainNavGraph(
                    navController = navController,
                    navigationActions = navigationActions
                )
            }
        }
    }

    private fun showNeedPermissionDialog(
        showDialog: Boolean,
        onConfirmClick: () -> Unit = {},
    ) {
        setContent {
            MusicRoadTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NeedPermissionDialog(
                        showDialog = showDialog,
                        onConfirmClick = onConfirmClick
                    )
                }
            }
        }
    }

    private fun showPermissionSnackbar() {
        setContent {
            MusicRoadTheme {
                PermissionSnackbar(
                    onActionPerformed = {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    },
                    onDismissed = {
                        showToast(getString(R.string.main_need_permissions))
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        )
        private const val REQUEST_PERMISSION_CODE = 1000
    }
}
