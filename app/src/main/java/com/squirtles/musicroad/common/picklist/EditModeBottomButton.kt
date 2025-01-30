package com.squirtles.musicroad.common.picklist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.R
import com.squirtles.musicroad.ui.theme.DarkGray
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.MusicRoadTheme
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun EditModeBottomButton(
    enabled: Boolean,
    deactivateEditMode: () -> Unit,
    showDeletePickDialog: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonHeight)
    ) {
        EditModeButton(
            text = stringResource(R.string.pick_list_deactivate_edit_mode_button_text),
            onClick = deactivateEditMode,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            buttonColor = Gray
        )

        EditModeButton(
            text = stringResource(R.string.pick_list_delete_selection_button_text),
            onClick = showDeletePickDialog,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            enabled = enabled
        )
    }
}

@Composable
private fun EditModeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true,
    textColor: Color = White,
    buttonColor: Color = Primary,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = buttonColor,
            contentColor = textColor,
            disabledContainerColor = DarkGray,
            disabledContentColor = White
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun EditModeBottomButtonPreview() {
    MusicRoadTheme {
        EditModeBottomButton(
            enabled = true,
            deactivateEditMode = {},
            showDeletePickDialog = {},
        )
    }
}

private val ButtonHeight = 48.dp
