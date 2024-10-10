package com.sonder.codechallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.domain.models.SearchItemRequest
import com.sonder.domain.models.SearchState
import com.sonder.domain.usecases.request.SearchRequestUseCase
import com.sonder.domain.usecases.search.SubscribeToSearchQueryUseCase
import com.sonder.domain.usecases.search.UpdateSearchStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val searchRequestUseCase: SearchRequestUseCase,
    private val subscribeToSearchQueryUseCase: SubscribeToSearchQueryUseCase,
    private val updateSearchStateUseCase: UpdateSearchStateUseCase,
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
            subscribeToSearchQueryUseCase.execute(Unit).collect { searchQuery ->
                if (searchQuery.isNotEmpty()) {
                    query = searchQuery

                    updateSearchStateUseCase.execute(SearchState.Searching)
                    delay(2000)

                    fetchSearchResults(searchQuery)

                    updateSearchStateUseCase.execute(SearchState.Idle)
                }
            }
        }
    }

    private fun fetchSearchResults(query: String) {
        viewModelScope.launch {
            searchRequestUseCase.execute(query)
                .catch { exception ->
                    _state.value = SearchFragmentStates.Error(message = exception.message ?: "Unknown error occurred")
                }
                .collect { updateResultsState(it) }
        }
    }

    private fun updateResultsState(searchItemRequest: SearchItemRequest) {
        if (searchItemRequest.sections == null) {
            _state.value = SearchFragmentStates.NoResults
        } else {
            _state.update { currentState ->
                val currentSections = (currentState as? SearchFragmentStates.ResultsLoaded)?.sections ?: emptyList()
                SearchFragmentStates.ResultsLoaded(sections = currentSections + searchItemRequest)
            }
        }
    }
}