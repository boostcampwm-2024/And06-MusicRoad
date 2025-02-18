package com.squirtles.musicroad.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.R
import com.squirtles.musicroad.ui.theme.Dark
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun CommentText(
    comment: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = comment.ifEmpty { stringResource(id = R.string.pick_comment_empty) },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(min = 100.dp)
            .padding(horizontal = 30.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(Dark)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodyLarge.copy(if (comment.isNotEmpty()) White else Gray)
    )
}

@Preview
@Composable
private fun CommentTextPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CommentText(comment = "")

        CommentText(comment = "노래가 좋아서 추천합니다.")

        CommentText(
            comment = "노래가 너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무" +
                    "너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무너무 좋아요"
        )
    }
}
