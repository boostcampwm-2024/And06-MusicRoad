package com.squirtles.musicroad.picklist

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.AlertStringDialog
import com.squirtles.musicroad.common.DialogTextButton
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.ui.theme.Primary

@Composable
internal fun DeleteSelectedPickDialog(
    selectedPickCount: Int,
    pickListType: PickListType,
    onDismissRequest: () -> Unit,
    onDeletePickClick: () -> Unit,
) {
    AlertStringDialog(
        onDismissRequest = onDismissRequest,
        title = stringResource(if (selectedPickCount == 0) R.string.default_alert_dialog_title else R.string.delete_pick_dialog_title),
        body = if (selectedPickCount == 0) {
            stringResource(R.string.no_selected_pick_dialog_body)
        } else {
            stringResource(
                when (pickListType) {
                    PickListType.FAVORITE -> R.string.delete_selected_favorite_pick_dialog_body
                    PickListType.CREATED -> R.string.delete_pick_dialog_body
                },
                selectedPickCount
            )
        },
        buttons = {
            if (selectedPickCount == 0) {
                DialogTextButton(
                    onClick = onDismissRequest,
                    text = stringResource(R.string.confirm_text)
                )
            } else {
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
            }
        },
    )
}
