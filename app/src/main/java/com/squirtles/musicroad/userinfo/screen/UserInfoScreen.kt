package com.squirtles.musicroad.userinfo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.COLOR_STOPS
import com.squirtles.musicroad.common.DefaultTopAppBar
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.common.VerticalSpacer
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White
import com.squirtles.musicroad.userinfo.UserInfoViewModel
import com.squirtles.musicroad.userinfo.components.MenuItem
import com.squirtles.musicroad.userinfo.components.UserInfoMenus

@Composable
fun UserInfoScreen(
    userId: String,
    onBackClick: () -> Unit,
    onBackToMapClick: () -> Unit,
    onFavoritePicksClick: (String) -> Unit,
    onMyPicksClick: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    onEditNotificationClick: () -> Unit,
    userInfoViewModel: UserInfoViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val user by userInfoViewModel.profileUser.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        userInfoViewModel.getUserById(userId)
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

                Image(
                    painter = painterResource(R.drawable.img_user_default_profile),
                    contentDescription = stringResource(R.string.user_info_default_profile_image),
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                VerticalSpacer(40)

                UserInfoMenus(
                    title = stringResource(R.string.user_info_pick_category_title),
                    menus = listOf(
                        MenuItem(
                            imageVector = Icons.Outlined.Archive,
                            contentDescription = stringResource(R.string.user_info_favorite_menu_icon_description),
                            menuTitle = stringResource(R.string.user_info_favorite_menu_title),
                            onMenuClick = { onFavoritePicksClick(userId) }
                        ),
                        MenuItem(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = stringResource(R.string.user_info_created_by_self_menu_icon_description),
                            menuTitle = stringResource(R.string.user_info_created_by_self_menu_title),
                            onMenuClick = { onMyPicksClick(userId) }
                        )
                    )
                )

                if (userId == userInfoViewModel.currentUser.userId) {
                    UserInfoMenus(
                        title = stringResource(R.string.user_info_setting_category_title),
                        menus = listOf(
                            MenuItem(
                                imageVector = Icons.Outlined.SwitchAccount,
                                contentDescription = stringResource(R.string.user_info_setting_profile_menu_icon_description),
                                menuTitle = stringResource(R.string.user_info_setting_profile_menu_title),
                                onMenuClick = onEditProfileClick
                            ),
                            MenuItem(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = stringResource(R.string.user_info_setting_notification_menu_icon_description),
                                menuTitle = stringResource(R.string.user_info_setting_notification_menu_title),
                                onMenuClick = onEditNotificationClick
                            )
                        )
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
        }
    }
}
