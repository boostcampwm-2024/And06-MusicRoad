package com.squirtles.musicroad.common.picklist.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.DialogTextButton
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.common.MessageAlertDialog
import com.squirtles.musicroad.common.picklist.PickListType
import com.squirtles.musicroad.ui.theme.Primary

@Composable
internal fun DeleteSelectedPickDialog(
    selectedPickCount: Int,
    pickListType: PickListType,
    onDismissRequest: () -> Unit,
    onDeletePickClick: () -> Unit,
) {
    MessageAlertDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.delete_pick_dialog_title),
        body = stringResource(
            when (pickListType) {
                PickListType.FAVORITE -> R.string.delete_selected_favorite_pick_dialog_body
                PickListType.CREATED -> R.string.delete_selected_pick_dialog_body
            },
            selectedPickCount
        ),
        buttons = {
            DialogTextButton(
                onClick = onDismissRequest,
                text = stringResource(R.string.delete_pick_dialog_cancel)
            )

            HorizontalSpacer(8)

            DialogTextButton(
                onClick = onDeletePickClick,
                text = stringResource(R.string.delete_pick_dialog_delete),
                textColor = Primary,
                fontWeight = FontWeight.Bold
            )
        },
    )
}
