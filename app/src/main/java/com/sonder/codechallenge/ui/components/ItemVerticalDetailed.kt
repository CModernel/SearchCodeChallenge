package com.sonder.codechallenge.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun ItemVerticalDetailed(
    imageUrl: String,
    title: String,
    description: String,
    contentType: String,
    onItemClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable(onClick = onItemClick)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = contentType,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Card(
            modifier = Modifier
                .size(width = 52.dp, height = 40.dp),
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Box(
                Modifier
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