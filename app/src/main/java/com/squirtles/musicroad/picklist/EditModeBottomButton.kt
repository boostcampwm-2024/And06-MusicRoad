package com.squirtles.musicroad.picklist

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
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.MusicRoadTheme
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun EditModeBottomButton(
    deactivateEditMode: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(ButtonHeight)
    ) {
        EditModeButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onClick = deactivateEditMode,
            text = stringResource(R.string.pick_list_deactivate_edit_mode_button_text),
            buttonColor = Gray
        )

        EditModeButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onClick = { TODO("선택 삭제") },
            text = stringResource(R.string.pick_list_delete_selection_button_text)
        )
    }
}

@Composable
private fun EditModeButton(
    modifier: Modifier,
    onClick: () -> Unit,
    text: String,
    textColor: Color = White,
    buttonColor: Color = Primary,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = buttonColor,
            contentColor = textColor
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
            deactivateEditMode = {},
        )
    }
}

private val ButtonHeight = 48.dp
