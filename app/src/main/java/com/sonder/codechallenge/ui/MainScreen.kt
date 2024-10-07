package com.sonder.codechallenge.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonder.codechallenge.R
import com.sonder.codechallenge.ui.components.ItemHorizontalCompact
import com.sonder.codechallenge.ui.components.ItemHorizontalDetailed
import com.sonder.codechallenge.ui.components.ItemVerticalCompact
import com.sonder.codechallenge.ui.components.ItemVerticalDetailed
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.common.capitalizeFirstLetter
import com.sonder.domain.models.SearchAction
import com.sonder.domain.models.SearchActionType
import com.sonder.domain.models.SearchContentType
import com.sonder.domain.models.SearchItem
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchItemViewType
import com.sonder.domain.models.SearchSectionResult

@Composable
fun MainScreenEntryPoint(viewModel: MainFragmentViewModel) {
    val state = viewModel.state.collectAsState()
    MainScreen(state = state)
}

@Composable
fun MainScreen(state: State<SearchFragmentStates>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        when (val currentState = state.value) {
            is SearchFragmentStates.Started -> {
                Text("Loading...")
            }

            is SearchFragmentStates.ResultsLoaded -> {
                Column(
                    modifier = Modifier.wrapContentSize()
                ) {
                    currentState.sections.forEach { section ->
                        SectionItem(sectionRequest = section)
                    }
                }
            }

            is SearchFragmentStates.Error -> {
                DisplayMessageSection(
                    stringResource(
                        R.string.an_error_has_ocurred_text,
                        currentState.message
                    ))
            }

            SearchFragmentStates.NoResults -> {
                DisplayMessageSection(stringResource(R.string.no_results_text))
            }
        }
    }
}

@Composable
fun DisplayMessageSection(message: String){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(message)
    }
}

@Composable
fun SectionItem(sectionRequest: SearchItemRequest) {
    val section = sectionRequest.sections
    section?.let {
        if (section.items.isEmpty())
            return

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = section.sectionTitle,
                color = colorResource(com.sonder.common.R.color.dark_blue),
                style = MaterialTheme.typography.headlineMedium,
            )
            section.sectionDescription?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            when (sectionRequest.searchType) {
                SearchItemViewType.VERTICAL_COMPACT -> VerticalCompactSection(section.items)
                SearchItemViewType.HORIZONTAL_DETAILED -> HorizontalDetailedSection(section.items)
                SearchItemViewType.VERTICAL_DETAILED -> VerticalDetailedSection(section.items)
                SearchItemViewType.HORIZONTAL_COMPACT -> HorizontalCompactSection(section.items)
                else -> return
            }
        }
    }
}

@Composable
fun HorizontalCompactSection(items: List<SearchItem>) {
    LazyRow {
        items(items.size) { index ->
            val item = items[index]
            ItemHorizontalCompact(
                title = item.title ?: "",
                onItemClick = { }
            )
        }
    }
}

@Composable
fun HorizontalDetailedSection(items: List<SearchItem>) {
    LazyRow {
        items(items.size) {
            val item = items[it]
            ItemHorizontalDetailed(
                imageUrl = item.thumbnail ?: "",
                title = item.title ?: "",
                description = item.description ?: "",
                ctaText = item.action?.title ?: "",
                contentType = item.contentType?.name ?: "",
                onItemClick = { }
            )
        }
    }
}

@Composable
fun VerticalDetailedSection(items: List<SearchItem>) {
    Column {
        items.forEach { item ->
            ItemVerticalDetailed(
                imageUrl = item.thumbnail ?: "",
                title = item.title ?: "",
                description = item.description ?: "",
                contentType = item.contentType?.value?.capitalizeFirstLetter() ?: "",
                onItemClick = { }
            )
        }
    }
}

@Composable
fun VerticalCompactSection(items: List<SearchItem>) {
    Column {
        items.forEach { item ->
            ItemVerticalCompact(
                title = item.title ?: "",
                onItemClick = { }
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        state = mutableStateOf(
            SearchFragmentStates.ResultsLoaded(
                sections = listOf(
                    SearchItemRequest(
                        searchType = SearchItemViewType.HORIZONTAL_COMPACT,
                        sections = SearchSectionResult(
                            sectionTitle = "Horizontal Detailed",
                            sectionDescription = "This section shows horizontal detailed items",
                            items = List(5) { index ->
                                SearchItem(
                                    id = "hd$index",
                                    title = "Horizontal Detailed Item $index",
                                    description = "This is a detailed description for item $index",
                                    contentType = SearchContentType.ARTICLE,
                                    thumbnail = "https://picsum.photos/200/300?random=$index",
                                    action = SearchAction(
                                        type = SearchActionType.ARTICLE,
                                        scheme = "Read More"
                                    )
                                )
                            }
                        ),
                    )
                )

            )
        )
    )
}