package com.sonder.codechallenge.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchItemViewType
import com.sonder.domain.models.SearchSectionResult
import com.sonder.domain.usecases.request.GetHorizontalCompactSearchResultsUseCase
import com.sonder.domain.usecases.request.GetHorizontalDetailedSearchResultsUseCase
import com.sonder.domain.usecases.request.GetVerticalCompactSearchResultsUseCase
import com.sonder.domain.usecases.request.GetVerticalDetailedSearchResultsUseCase
import com.sonder.domain.usecases.search.SubscribeToSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val getHorizontalCompactSearchResultsUseCase: GetHorizontalCompactSearchResultsUseCase,
    private val getVerticalCompactSearchResultsUseCase: GetVerticalCompactSearchResultsUseCase,
    private val getHorizontalDetailedSearchResultsUseCase: GetHorizontalDetailedSearchResultsUseCase,
    private val getVerticalDetailedSearchResultsUseCase: GetVerticalDetailedSearchResultsUseCase,
    private val subscribeToSearchQueryUseCase: SubscribeToSearchQueryUseCase
) : ViewModel() {

    private var query: String? = null

    // Define fragment states class
    private val _state: MutableStateFlow<SearchFragmentStates> = MutableStateFlow(SearchFragmentStates.Started)
    val state = _state.asStateFlow()

    init {
        subscribeToSearchQuery()
    }

    private fun subscribeToSearchQuery() {
        viewModelScope.launch {
            subscribeToSearchQueryUseCase.execute(Unit).collect {
                if (it.isNotEmpty()) {
                    query = it

                    when(query){
                        "mock" -> fetchSearchResults()
                        // TODO: "error" -> call use case and update state with error
                        // TODO: else -> update state to no results
                    }

                }
            }
        }
    }

    private fun fetchSearchResults() {
        viewModelScope.launch {
            delay(2000)
            fetchSearchResultsByType(SearchItemViewType.HORIZONTAL_COMPACT)
            fetchSearchResultsByType(SearchItemViewType.VERTICAL_COMPACT)
            fetchSearchResultsByType(SearchItemViewType.HORIZONTAL_DETAILED)
            fetchSearchResultsByType(SearchItemViewType.VERTICAL_DETAILED)
        }
    }

    private fun fetchSearchResultsByType(viewType: SearchItemViewType) {
        viewModelScope.launch {
            val result: SearchSectionResult? = when (viewType) {
                SearchItemViewType.HORIZONTAL_COMPACT -> getHorizontalCompactSearchResultsUseCase.execute(Unit).firstOrNull()
                SearchItemViewType.VERTICAL_COMPACT -> getVerticalCompactSearchResultsUseCase.execute(Unit).firstOrNull()
                SearchItemViewType.HORIZONTAL_DETAILED -> getHorizontalDetailedSearchResultsUseCase.execute(Unit).firstOrNull()
                SearchItemViewType.VERTICAL_DETAILED -> getVerticalDetailedSearchResultsUseCase.execute(Unit).firstOrNull()
            }

            result?.let {
                _state.update {
                    val currentSections = (_state.value as? SearchFragmentStates.ResultsLoaded)?.sections ?: emptyList()

                    val updatedSections = if (currentSections.any { it.searchType == viewType }) {
                        currentSections.map { section ->
                            if (section.searchType == viewType) section.copy(sections = result) else section
                        }
                    } else {
                        currentSections + SearchItemRequest(viewType, result)
                    }

                    SearchFragmentStates.ResultsLoaded(sections = updatedSections)
                }
            }
        }
    }
}