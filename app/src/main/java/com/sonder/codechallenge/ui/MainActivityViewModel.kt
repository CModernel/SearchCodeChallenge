package com.sonder.codechallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonder.codechallenge.ui.models.SearchActivityStates
import com.sonder.domain.usecases.search.ClearSearchResultsUseCase
import com.sonder.domain.usecases.search.UpdateSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val updateSearchQueryUseCase: UpdateSearchQueryUseCase,
    private val clearSearchResultsUseCase: ClearSearchResultsUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<SearchActivityStates> = MutableStateFlow(
        SearchActivityStates.Started)
    val state = _state.asStateFlow()

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            updateSearchQueryUseCase.execute(query)
            _state.value = SearchActivityStates.Searching(query)
        }
    }

    fun clearSearchQuery() {
        clearSearchResults()
        _state.value = SearchActivityStates.Started
    }

    private fun clearSearchResults() {
        viewModelScope.launch {
            clearSearchResultsUseCase.execute(Unit)
        }
    }
}