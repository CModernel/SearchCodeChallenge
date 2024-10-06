package com.sonder.codechallenge.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonder.codechallenge.R

@Composable
fun ItemHorizontalCompact(
    title: String,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(dimensionResource(R.dimen.spaceX13))
            .height(dimensionResource(R.dimen.spaceX10))
            .padding(end = dimensionResource(R.dimen.space))
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(dimensionResource(R.dimen.space))
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.space)),
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ItemHorizontalCompactPreview() {
    ItemHorizontalCompact(
        title = stringResource(R.string.addiction_and_dependency),
        onItemClick = {},
    )
}