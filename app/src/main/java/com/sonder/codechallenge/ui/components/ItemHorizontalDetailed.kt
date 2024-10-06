package com.sonder.codechallenge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonder.codechallenge.R

@Composable
fun ItemHorizontalDetailed(
    imageUrl: String,
    title: String,
    description: String,
    ctaText: String,
    contentType: String,
    onItemClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .width(124.dp)
            .fillMaxHeight()
            .padding(end = 6.dp)
            .clickable(onClick = onItemClick)
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            if (imageUrl.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_article),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_video),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center)
            )
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Text(
            text = description,
            maxLines = 8,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ctaText,
                color = Color(0xFF6200EE),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_video),
                contentDescription = null,
                tint = Color(0xFF018786),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = contentType,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ItemHorizontalDetailedPreview() {
    ItemHorizontalDetailed(
        imageUrl = "",
        title = stringResource(R.string.lorem),
        description = stringResource(R.string.lorem),
        ctaText = stringResource(R.string.addiction_and_dependency),
        contentType = stringResource(R.string.addiction_and_dependency),
        onItemClick = {},
    )
}