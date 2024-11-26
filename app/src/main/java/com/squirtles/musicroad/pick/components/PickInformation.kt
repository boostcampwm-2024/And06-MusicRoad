package com.squirtles.musicroad.pick.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.R
import com.squirtles.musicroad.ui.theme.Gray

@Composable
internal fun PickInformation(formattedDate: String, favoriteCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (formattedDate.isNotBlank()) {
            Text(text = formattedDate, style = MaterialTheme.typography.titleMedium.copy(Gray))
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = stringResource(R.string.pick_favorite_count_icon_description),
                modifier = Modifier.padding(start = 4.dp),
                tint = Gray
            )
            Text(text = "$favoriteCount", style = MaterialTheme.typography.titleMedium.copy(Gray))
        }
    }
}
