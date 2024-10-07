package com.sonder.codechallenge.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sonder.codechallenge.R

@Composable
fun ItemHorizontalDetailed(
    imageUrl: String,
    title: String,
    description: String,
    ctaText: String,
    contentType: String,
    modifier: Modifier = Modifier,
    icon: Int? = null,
    onItemClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(220.dp)
            .heightIn(max = 300.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .clickable(onClick = onItemClick)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp)
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
            if(icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = colorResource(com.sonder.common.R.color.dark_blue),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = description,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray,
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = ctaText,
            modifier = Modifier.padding(8.dp).align(Alignment.Start),
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 16.sp,
            ),
        )

    }
}

@Preview
@Composable
private fun ItemHorizontalDetailedPreview() {
    ItemHorizontalDetailed(
        imageUrl = "",
        icon = R.drawable.ic_video,
        title = "Looking after your mental health",
        description = "Take this confidential 3 minute assessment, and we’ll provide you with personalised support to boost your overall wellbeing",
        ctaText = "Read Article now",
        contentType = stringResource(R.string.addiction_and_dependency),
        onItemClick = {},
    )
}