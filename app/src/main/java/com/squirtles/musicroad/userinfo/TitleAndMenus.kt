package com.squirtles.musicroad.userinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.DEFAULT_PADDING
import com.squirtles.musicroad.common.VerticalSpacer
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun TitleAndMenus(
    title: String,
    titleTextColor: Color = White,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    menus: List<MenuItem>,
    menuTextColor: Color = White,
    menuTextStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Column {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MENU_PADDING_HORIZONTAL,
                    vertical = MENU_PADDING_VERTICAL
                ),
            color = titleTextColor,
            fontWeight = FontWeight.Bold,
            style = titleTextStyle
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MENU_PADDING_HORIZONTAL),
            color = Gray
        )

        VerticalSpacer(8)

        for (menu in menus) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = White),
                        onClick = menu.onMenuClick
                    )
                    .padding(
                        horizontal = MENU_PADDING_HORIZONTAL,
                        vertical = MENU_PADDING_VERTICAL
                    ),
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_PADDING),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = menu.imageVector,
                    contentDescription = menu.contentDescription,
                    tint = menu.iconColor
                )
                Text(
                    text = menu.menuTitle,
                    modifier = Modifier.weight(1f),
                    color = menuTextColor,
                    style = menuTextStyle
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = stringResource(R.string.user_info_navigate_to_menu),
                    modifier = Modifier.size(16.dp),
                    tint = Gray
                )
            }
        }
    }
}

private val MENU_PADDING_HORIZONTAL = 24.dp
private val MENU_PADDING_VERTICAL = 8.dp
