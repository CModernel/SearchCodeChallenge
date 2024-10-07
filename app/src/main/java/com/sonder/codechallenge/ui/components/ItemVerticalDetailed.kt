package com.sonder.codechallenge.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sonder.codechallenge.R

@Composable
fun ItemVerticalDetailed(
    imageUrl: String,
    title: String,
    description: String,
    contentType: String,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    onItemClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable(onClick = onItemClick)
            .background(Color.White, RoundedCornerShape(8.dp)),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(6.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = colorResource(com.sonder.common.R.color.dark_blue),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                maxLines = 2,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = contentType,
                maxLines = 1,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }


        Box(
            Modifier
                .padding(4.dp)
                .size(width = 100.dp, height = 100.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .listener(
                            onStart = { Log.d("Coil", "Image loading started") },
                            onSuccess = { _, _ -> Log.d("Coil", "Image loaded successfully") },
                            onError = { _, throwable ->
                                Log.e(
                                    "Coil",
                                    "Image loading failed",
                                    throwable.throwable
                                )
                            },
                        )
                        .build(),
                    contentDescription = contentType,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
        }

    }
}

@Preview
@Composable
private fun ItemVerticalDetailedPreview() {
    ItemVerticalDetailed(
        imageUrl = "",
        title = stringResource(R.string.lorem),
        description = stringResource(R.string.lorem),
        contentType = stringResource(R.string.addiction_and_dependency),
        onItemClick = {},
    )
}