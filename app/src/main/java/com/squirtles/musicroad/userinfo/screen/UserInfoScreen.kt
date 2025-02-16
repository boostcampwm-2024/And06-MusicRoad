package com.squirtles.musicroad.userinfo.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SwitchAccount
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.squirtles.musicroad.R
import com.squirtles.musicroad.account.AccountViewModel
import com.squirtles.musicroad.account.GoogleId
import com.squirtles.musicroad.common.Constants.COLOR_STOPS
import com.squirtles.musicroad.common.DefaultTopAppBar
import com.squirtles.musicroad.common.DialogTextButton
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.common.MessageAlertDialog
import com.squirtles.musicroad.common.VerticalSpacer
import com.squirtles.musicroad.ui.theme.DarkGray
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White
import com.squirtles.musicroad.userinfo.UserInfoViewModel
import com.squirtles.musicroad.userinfo.components.MenuItem
import com.squirtles.musicroad.userinfo.components.UserInfoMenus
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(
    uid: String,
    onBackClick: () -> Unit,
    onBackToMapClick: () -> Unit,
    onFavoritePicksClick: (String) -> Unit,
    onMyPicksClick: (String) -> Unit,
    onEditProfileClick: (String) -> Unit,
    onEditNotificationClick: () -> Unit,
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState = rememberScrollState()
    val user by userInfoViewModel.profileUser.collectAsStateWithLifecycle()

    var showLogOutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    val onSignOutClick: () -> Unit = {
        GoogleId(context).signOut()
        accountViewModel.signOut()
    }

    val onDeleteAccountClick: () -> Unit = {
        GoogleId(context).signOut()
        accountViewModel.deleteAccount()
    }

    LaunchedEffect(Unit) {
        uid.let {
            userInfoViewModel.getUserById(uid)
        }

        launch {
            accountViewModel.signOutSuccess
                .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { isSuccess ->
                    if (isSuccess) {
                        onBackToMapClick()
                    }
                }
        }

        launch {
            accountViewModel.deleteAccountSuccess
                .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { isSuccess ->
                    if (isSuccess) {
                        onBackToMapClick()
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = user.userName,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops = COLOR_STOPS))
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 96.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer(16)

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.userProfileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.user_info_profile_image),
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.img_user_default_profile),
                    error = painterResource(R.drawable.img_user_default_profile),
                    contentScale = ContentScale.Crop,
                )

                VerticalSpacer(40)

                UserInfoMenus(
                    title = stringResource(R.string.user_info_pick_category_title),
                    menus = listOf(
                        MenuItem(
                            imageVector = Icons.Outlined.Archive,
                            contentDescription = stringResource(R.string.user_info_favorite_menu_icon_description),
                            menuTitle = stringResource(R.string.user_info_favorite_menu_title),
                            onMenuClick = { onFavoritePicksClick(uid) }
                        ),
                        MenuItem(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = stringResource(R.string.user_info_created_by_self_menu_icon_description),
                            menuTitle = stringResource(R.string.user_info_created_by_self_menu_title),
                            onMenuClick = { onMyPicksClick(uid) }
                        )
                    )
                )

                if (uid == userInfoViewModel.currentUid) {
                    UserInfoMenus(
                        title = stringResource(R.string.user_info_setting_category_title),
                        menus = listOf(
                            MenuItem(
                                imageVector = Icons.Outlined.SwitchAccount,
                                contentDescription = stringResource(R.string.user_info_setting_profile_menu_icon_description),
                                menuTitle = stringResource(R.string.user_info_setting_profile_menu_title),
                                onMenuClick = { onEditProfileClick(user.userName) }
                            ),
                            MenuItem(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = stringResource(R.string.user_info_setting_notification_menu_icon_description),
                                menuTitle = stringResource(R.string.user_info_setting_notification_menu_title),
                                onMenuClick = onEditNotificationClick
                            ),
                            MenuItem(
                                imageVector = Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = stringResource(R.string.user_info_setting_sign_out_menu_icon_description),
                                menuTitle = stringResource(R.string.user_info_setting_sign_out_menu_title),
                                onMenuClick = { showLogOutDialog = true }
                            )
                        )
                    )

                    // 회원 탈퇴
                    Text(
                        text = stringResource(id = R.string.user_info_setting_delete_user_account),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clickable { showDeleteAccountDialog = true },
                        color = DarkGray,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            ExtendedFloatingActionButton(
                onClick = onBackToMapClick,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 48.dp)
                    .align(Alignment.BottomCenter),
                shape = CircleShape,
                containerColor = Primary,
                contentColor = White,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Map,
                    contentDescription = stringResource(R.string.user_info_icon_map_description),
                    tint = White
                )

                HorizontalSpacer(8)

                Text(
                    text = stringResource(R.string.user_info_back_to_map_button_text),
                    color = White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (showLogOutDialog) {
                MessageAlertDialog(
                    onDismissRequest = {
                        showLogOutDialog = false
                    },
                    title = stringResource(R.string.sign_out_dialog_title),
                    body = "",
                    showBody = false
                ) {
                    DialogTextButton(
                        onClick = {
                            showLogOutDialog = false
                        },
                        text = stringResource(R.string.sign_out_dialog_dismiss)
                    )

                    HorizontalSpacer(8)

                    DialogTextButton(
                        onClick = {
                            showLogOutDialog = false
                            onSignOutClick()
                        },
                        text = stringResource(R.string.sign_out_dialog_confirm),
                        textColor = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showDeleteAccountDialog) {
                MessageAlertDialog(
                    onDismissRequest = {
                        showDeleteAccountDialog = false
                    },
                    title = stringResource(R.string.delete_account_dialog_title),
                    body = stringResource(R.string.delete_account_dialog_description),
                    showBody = true
                ) {
                    DialogTextButton(
                        onClick = {
                            showDeleteAccountDialog = false
                        },
                        text = stringResource(R.string.delete_account_dialog_dismiss)
                    )

                    HorizontalSpacer(8)

                    DialogTextButton(
                        onClick = {
                            showDeleteAccountDialog = false
                            onDeleteAccountClick()
                        },
                        text = stringResource(R.string.delete_account_dialog_confirm),
                        textColor = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
