package com.squirtles.musicroad.common.picklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.squirtles.domain.model.Song
import com.squirtles.musicroad.common.AlbumImage
import com.squirtles.musicroad.common.CommentText
import com.squirtles.musicroad.common.Constants
import com.squirtles.musicroad.common.CreatedByOtherUserText
import com.squirtles.musicroad.common.FavoriteCountText
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.common.SongInfoText
import com.squirtles.musicroad.ui.theme.Gray
import com.squirtles.musicroad.ui.theme.White

@Composable
fun PickListItem(
    song: Song,
    isCreatedByOthers: Boolean,
    createUserName: String,
    favoriteCount: Int,
    comment: String,
    createdAt: String,
    onItemClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = White),
            ) { onItemClick() }
            .padding(
                horizontal = Constants.DEFAULT_PADDING,
                vertical = Constants.DEFAULT_PADDING / 2
            ),
        horizontalArrangement = Arrangement.spacedBy(Constants.DEFAULT_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AlbumImage(
            imageUrl = song.getImageUrlWithSize(Constants.REQUEST_IMAGE_SIZE_DEFAULT),
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            SongInfoText(
                songInfo = "${song.songName} - ${song.artistName}",
                color = White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCreatedByOthers) {
                    CreatedByOtherUserText(
                        userName = createUserName,
                        modifier = Modifier.weight(weight = 1f, fill = false),
                        color = Gray
                    )
                } else {
                    Text(
                        text = createdAt,
                        modifier = Modifier.weight(weight = 1f, fill = false),
                        color = Gray,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                HorizontalSpacer(8)

                FavoriteCountText(
                    favoriteCount = favoriteCount,
                    iconTint = Gray,
                    color = Gray
                )
            }

            CommentText(
                comment = comment,
                color = Gray
            )
        }
    }
}
