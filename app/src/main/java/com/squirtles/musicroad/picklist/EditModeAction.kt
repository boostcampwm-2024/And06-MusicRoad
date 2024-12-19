package com.squirtles.musicroad.picklist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.squirtles.musicroad.R
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun EditModeAction(
    isEditMode: Boolean,
    enabled: Boolean,
    activateEditMode: () -> Unit,
) {
    if (isEditMode) {
        TextButton(
            onClick = { TODO("픽 목록 전체 선택") }
        ) {
            Text(
                text = stringResource(R.string.pick_list_select_all_button_text),
                color = White,
            )
        }
    } else {
        IconButton(
            onClick = activateEditMode,
            enabled = enabled,
            colors = IconButtonDefaults.iconButtonColors().copy(
                contentColor = White,
                disabledContentColor = Gray,
            )
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.pick_list_activate_edit_mode_button_description),
            )
        }
    }
}
