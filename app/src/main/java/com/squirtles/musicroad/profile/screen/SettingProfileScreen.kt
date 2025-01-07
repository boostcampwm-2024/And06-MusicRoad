package com.squirtles.musicroad.profile.screen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.COLOR_STOPS
import com.squirtles.musicroad.profile.ProfileViewModel
import com.squirtles.musicroad.ui.theme.Black
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.MusicRoadTheme
import com.squirtles.musicroad.ui.theme.White
import kotlinx.coroutines.delay
import java.util.regex.Pattern

@Composable
internal fun SettingProfileScreen(
    onBackClick: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val userName = remember { mutableStateOf(profileViewModel.currentUser.userName) }
    val nickNameErrorMessage = remember { mutableStateOf("") }
    var showCreateIndicator by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showCreateIndicator) { }

    LaunchedEffect(Unit) {
        profileViewModel.updateSuccess
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { isSuccess ->
                focusManager.clearFocus()
                delay(100)
                if (isSuccess) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.setting_profile_update_nickname_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackClick()
                } else {
                    showCreateIndicator = false
                    Toast.makeText(
                        context,
                        context.getString(R.string.setting_profile_update_nickname_failure),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    Scaffold(
        topBar = {
            SettingProfileAppBar(
                confirmEnabled = nickNameErrorMessage.value.isEmpty() &&
                        profileViewModel.currentUser.userName != userName.value,
                onConfirmClick = {
                    showCreateIndicator = true
                    profileViewModel.updateUsername(userName.value)
                },
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops = COLOR_STOPS))
                .padding(innerPadding)
        ) {
            SettingProfileContent(userName, nickNameErrorMessage)
        }
    }

    if (showCreateIndicator) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black.copy(alpha = 0.5F))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingProfileAppBar(
    confirmEnabled: Boolean,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.setting_profile_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.top_app_bar_back_description),
                    tint = White
                )
            }
        },
        actions = {
            IconButton(
                onClick = onConfirmClick,
                enabled = confirmEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.setting_profile_confirm_icon_description),
                    tint = if (confirmEnabled) White else Gray
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = Color.Transparent,
            titleContentColor = White
        )
    )
}

private fun validateUserName(userName: String, context: Context) = when {
    userName.length < 2 -> context.getString(R.string.setting_profile_nickname_message_length_fail_min)
    userName.length > 10 -> context.getString(R.string.setting_profile_nickname_message_length_fail_max)
    Pattern.matches(USERNAME_PATTERN, userName).not() -> context.getString(R.string.setting_profile_nickname_message_format_fail)
    else -> ""
}

@Composable
private fun SettingProfileContent(
    userName: MutableState<String>,
    nickNameErrorMessage: MutableState<String>
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 30.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.setting_profile_nickname),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = userName.value,
            onValueChange = {
                nickNameErrorMessage.value = validateUserName(it, context)
                userName.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = { Text(nickNameErrorMessage.value) },
            isError = nickNameErrorMessage.value.isNotEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = White,
                unfocusedTextColor = White,
                errorTextColor = White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = White,
                focusedBorderColor = Gray,
                unfocusedBorderColor = Gray,
            )
        )
    }
}

private const val USERNAME_PATTERN = "^[ㄱ-ㅎ|ㅏ-ㅣ가-힣a-zA-Z0-9]+$"

@Preview
@Composable
private fun SettingProfileAppBarPreview() {
    SettingProfileAppBar(false, {}, {})
}

@Preview
@Composable
private fun SettingProfileContentPreview() {
    MusicRoadTheme {
        SettingProfileContent(
            remember { mutableStateOf("짱구") },
            remember { mutableStateOf("") }
        )
    }
}
