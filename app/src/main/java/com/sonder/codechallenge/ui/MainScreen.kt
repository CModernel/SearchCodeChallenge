package com.sonder.codechallenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sonder.codechallenge.ui.models.SearchFragmentStates

@Composable
fun MainScreenEntryPoint(viewModel: MainFragmentViewModel) {
    val state = viewModel.state.collectAsState()
    MainScreen(state = state)
}

@Composable
fun MainScreen(state: State<SearchFragmentStates>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Loading...")
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        state =
        mutableStateOf(
            SearchFragmentStates.ResultsLoaded(
                sections = listOf()
            )
        )
    )
}